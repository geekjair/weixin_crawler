package com.weixincrawl.pipeline;

import javax.sql.DataSource;
import com.weixincrawl.oo.Article;
import us.codecraft.webmagic.Task;
import us.codecraft.webmagic.pipeline.PageModelPipeline;

public class DBModelPipeline implements PageModelPipeline<Article> {
	
	public void process(Article t, Task task) {
		// TODO Auto-generated method stub
		System.out.println(t.getTitle());
		
	}

}
