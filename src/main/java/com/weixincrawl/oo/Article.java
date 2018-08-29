package com.weixincrawl.oo;

import java.util.List;

import com.weixincrawl.common.Column;

import us.codecraft.webmagic.model.annotation.ExtractBy;
import us.codecraft.webmagic.model.annotation.ExtractByUrl;
import us.codecraft.webmagic.model.annotation.HelpUrl;
import us.codecraft.webmagic.model.annotation.TargetUrl;

//sourceRegion是确定url页面的哪个片段里抽取
@TargetUrl(sourceRegion = "//div[@class='news-box']/ul[@class='news-list']/li" ,value = {"http://mp.weixin.qq.com/s\\?*"})
@HelpUrl(value = { "http://weixin.sogou.com/weixin\\?query=*" })
@Column("wx_article")
public class Article {
	
	@ExtractBy(value = "//*[@id='copyright_logo']")
	private String copyright_tag;
	
	@ExtractBy(value = "//*[@id='activity-name']")
	private String title;
	
	@ExtractBy(value="//*[@id='profileBt']")
	private String author;
	
	@ExtractBy(value="//*[@id='publish_time']")
	private String publish_time;
	
	@ExtractBy(value = "//*[@id='js_content']")
	private String content;
	
	@ExtractBy(value = "//*[@id='js_content']//img/@data-src")
	private List imagesUrlList;//只读,便于下载图片
	
	@ExtractBy(value="")
	private String html;//只读,便于测试
	
	private Integer id;
	
	public String getCopyright_tag() {
		return copyright_tag;
	}
	public String getTitle() {
		return title;
	}
	public String getAuthor() {
		return author;
	}
	
	
	public String getContent() {
		return content;
	}
	public List getImagesUrlList() {
		return imagesUrlList;
	}
	public String getHtml() {
		return html;
	}
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPublish_time() {
		return publish_time;
	}
	public void setPublish_time(String publish_time) {
		this.publish_time = publish_time;
	}
	public void setCopyright_tag(String copyright_tag) {
		this.copyright_tag = copyright_tag;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public void setAuthor(String author) {
		this.author = author;
	}
	public void setContent(String content) {
		this.content = content;
	}

}
