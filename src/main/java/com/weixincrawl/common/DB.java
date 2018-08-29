package com.weixincrawl.common;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.sql.DataSource;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.BeanHandler;
import org.apache.commons.dbutils.handlers.MapHandler;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.builder.ToStringBuilder;

import com.mchange.v2.c3p0.DataSources;
import com.weixincrawl.oo.Article;




public class DB {
	
	private static DataSource ds = null;
	private static QueryRunner qr = null;
	private static DB instance = new DB();
	
	private  DB(){
		if(ds == null) {
			try {
				Properties prop = new PropertyConfigerator().getDbConfigure();
				ds =DataSources.unpooledDataSource(prop.getProperty("url"), prop.getProperty("username"), prop.getProperty("password"));
				qr = new QueryRunner(ds);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public static DB getDB(){
		return instance;
	}
	
	/**
	 * insert  one for class
	 * @param clazz
	 * @param values
	 * @return
	 */
	public  static boolean insert(Class clazz,Map<String,Object> values){
		String columnString;
		columnString = StringUtils.join(values.keySet(), "`,`");
		//拼接SQL
		Annotation annotation = clazz.getAnnotation(Column.class);
		String tableName = clazz.getName();//默认表名
		if (annotation != null) {
			Column columnName = (Column) annotation;
			tableName = columnName.value();
        }
		int valueSize = values.size();
		Object[] obj = new Object[valueSize];
		List<String> virtualParams = new ArrayList();
		int i = 0;
		for(String column : values.keySet()){
			obj[i] = values.get(column);
			virtualParams.add("?");
			i++;
		}
		String sql = "insert into "+tableName+"(`"+columnString+"`) values ("+StringUtils.join(virtualParams,",")+")";
		Map movie_insert = null;
		try {
			 movie_insert = qr.insert(sql, new MapHandler(),obj );
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if(MapUtils.isNotEmpty(movie_insert)){
			return true;
		}
		return false;
	}
	
	/**
	 * 参数class
	 * @param <T>
	 * @return 
	 * @return 
	 */
	public static  <T> T query(T t){
		Class clazz = t.getClass();//获取相应的类
		 T result = null;
		 Annotation annotation = t.getClass().getAnnotation(Column.class);
		String tableName = t.getClass().getName();//默认表名
		if (annotation != null) {
			Column columnName = (Column) annotation;
			tableName = columnName.value();
        }
		Map<String,Object> valueMap = new HashMap<String,Object>();
		//获取成员变量
		Field[] fileds = t.getClass().getDeclaredFields();
		for(Field field : fileds){
			try {
				field.setAccessible(true);
				Object obj = field.get(t);
				if(obj != null){
					valueMap.put(field.getName(), obj);
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		String sql = "SELECT * FROM "+ tableName;
		String condition = "";
		if(valueMap.size() > 0){
			List<String> conditionList = new ArrayList();
			for(String column : valueMap.keySet()){
				conditionList.add("`"+column+"` = ?");
			}
			sql += " WHERE " + StringUtils.join(conditionList," AND ");
		}
		//Map result = null;
		try {
			result = qr.query(sql, new BeanHandler<T>(clazz),valueMap.values().toArray());
		} catch (SQLException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return result;
	}
	
	public static void main(String[] args){
		DB db = DB.getDB();
		Article a = new Article();
		//a.setId(1);
		Article s = db.query(a);
		  System.out.println(ToStringBuilder.reflectionToString(s));
	}
}
