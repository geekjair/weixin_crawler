package com.weixincrawl.main;

import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.weixincrawl.oo.Article;
import com.weixincrawl.pipeline.DBModelPipeline;
import com.weixincrawl.pipeline.FileModelPipeline;

import us.codecraft.webmagic.Site;
import us.codecraft.webmagic.downloader.HttpClientDownloader;
import us.codecraft.webmagic.model.ConsolePageModelPipeline;
import us.codecraft.webmagic.model.OOSpider;
import us.codecraft.webmagic.proxy.Proxy;
import us.codecraft.webmagic.proxy.SimpleProxyProvider;

public class Main {
	public static void main(String[] args) {
		
		//1.create proxy
		HttpClientDownloader httpClientDownloader = new HttpClientDownloader();
	    httpClientDownloader.setProxyProvider(SimpleProxyProvider.from(new Proxy("127.0.0.1",8888)));
	    //2.add set need suv and snuid
	    final Site site = new Site();
	    site.addHeader("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36");
	    site.addHeader("Referer", "http://weixin.sogou.com/weixin?type=2&s_from=input&query=%E6%9E%B6%E6%9E%84%E5%B8%88&ie=utf8&_sug_=n&_sug_type_=");
	    site.addCookie("SUV", "003B178272FFF78B5B4312FAC92A5521");
	    site.addCookie("SNUID", "DEA1A9245650213382FD160556B37974");//当发生验证码拦截，输入正确的验证会更新snuid
	    OOSpider.create(site
                , new FileModelPipeline(), Article.class)
       //.addPageModel(new DBModelPipeline(), Article.class)
        //.setDownloader(httpClientDownloader)
                .addUrl("http://weixin.sogou.com/weixin?type=2&s_from=input&query=JVM&ie=utf8&_sug_=n&_sug_type_=").thread(5).run();
	    
	    //更新Site
	   new Thread(new Runnable() {
		
		public void run() {
			while(true){
				try {
					Thread.sleep(50000);
					// TODO Auto-generated method stub
					int sleepTime = new Random().nextInt(50000);
					site.setSleepTime(sleepTime);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
	}).start();
	   
    }
	
	public static void testRegex(){
		String s = "http://mp.weixin.qq.com/s?src=11&timestamp=1534505948&ver=1066&signature=QqZMPRhrWLqgl1ghvR4JcVjEIYMmv9e1SvV69I4pALJCk4jtN9B3N-OYMiViLRFz-JyeQIwzsiFfJn0FtENyRBIIO5B0E7VvA*1LKXgwMlLVw5ItwltEcrxUxdjQ-7q8&new=1";
		Pattern p =Pattern.compile("http://mp\\.weixin\\.qq\\.com/s\\?.+");
		Matcher matcher = p.matcher(s);
        if (matcher.find()) {
            System.out.println(matcher.group(0));
        }
	}
}
