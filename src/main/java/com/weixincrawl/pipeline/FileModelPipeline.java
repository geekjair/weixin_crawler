package com.weixincrawl.pipeline;

import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.lang3.StringUtils;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import com.weixincrawl.oo.Article;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;
import us.codecraft.webmagic.selector.Html;
import us.codecraft.webmagic.selector.RegexSelector;
import us.codecraft.webmagic.selector.XpathSelector;
import us.codecraft.webmagic.utils.FilePersistentBase;

public class FileModelPipeline extends FilePersistentBase implements PageModelPipeline<Article> {
	
	public void process(Article t, Task task) {
		String date = new SimpleDateFormat("yyyyMMdd").format(new Date());
		String path = "D:\\wx_crawler\\"+date+"\\";
		// TODO Auto-generated method stub
		//替换
		String content = t.getContent();
		content = content.replaceAll("\\.*(data-src)=\\.*", "src=");
		//需要将图片保存下来且替换掉文章内容目录下
		List<String> imagesUrls = t.getImagesUrlList();
		String title = t.getTitle();
		String title_new = new XpathSelector("//h2[@class='rich_media_title']/text()").select(title);
		if(StringUtils.isNotBlank(title_new)){
			title = title_new;
		}
		HashMap<String,String> resultMap = new HashMap<String,String> ();
		//title替换
		content = title.replaceAll("\\.*|\\.*", "");
		try {
			resultMap = getImage(imagesUrls,path + StringUtils.trim(title)+PATH_SEPERATOR);
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		PrintWriter printWriter = null;
		try {
			printWriter = new PrintWriter(new FileWriter(getFile(path + title + ".html")));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	printWriter.write(content);
         printWriter.close();
		
	}
	
	public static HashMap<String, String> getImage(List<String> urlList,String path) throws IOException{
		
		HashMap<String,String> resultMap = new HashMap<String,String> ();
		HttpClientBuilder client = HttpClientBuilder.create();
		int i =0;
		File myPath = new File(path);
		   if ( !myPath.exists()){//若此目录不存在，则创建之// 这个东西只能简历一级文件夹，两级是无法建立的。。。。。
		           myPath.mkdir();
		           //System.out.println("创建文件夹路径为："+ path);
		}
		for(String url : urlList){
			//3,设置请求方式
			//String title_new = new RegexSelector("http://(*)?").select(url);
			HttpGet get = new HttpGet(url);
		
		//4,执行请求, 获取响应信息
			HttpResponse response = client.build().execute(get);
			
			if(response.getStatusLine().getStatusCode() == 200)
			{
				//得到实体
				HttpEntity entity = response.getEntity();
				
				byte[] data = EntityUtils.toByteArray(entity);
				
				//图片存入磁盘
				FileOutputStream fos = new FileOutputStream(path+i+".png");
				fos.write(data);
				fos.close();
				resultMap.put(url,path);
				i++;
			}
		}
		
			return resultMap;
	}

}
