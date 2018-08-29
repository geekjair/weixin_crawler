package com.weixincrawl.common;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Properties;

import org.apache.log4j.helpers.LogLog;
import org.apache.log4j.lf5.DefaultLF5Configurator;
import org.apache.log4j.spi.LoggerRepository;

public class PropertyConfigerator {
	  static final String       DB_Properties= "db.properties";
	  
	  public Properties getDbConfigure(){
			    URL configFileResource =
			    		getClass().getClassLoader().getResource(DB_Properties);
		  return doConfigure(configFileResource);
	  }
	  
	private  Properties doConfigure(String configFileName) {
	    Properties props = new Properties();
	    FileInputStream istream = null;
	    try {
	      istream = new FileInputStream(configFileName);
	      props.load(istream);
	      istream.close();
	    }
	    catch (Exception e) {
	      if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
	          Thread.currentThread().interrupt();
	      }
	      LogLog.error("Could not read configuration file ["+configFileName+"].", e);
	      LogLog.error("Ignoring configuration file [" + configFileName+"].");
	      return props;
	    } finally {
	        if(istream != null) {
	            try {
	                istream.close();
	            } catch(InterruptedIOException ignore) {
	                Thread.currentThread().interrupt();
	            } catch(Throwable ignore) {
	            }

	        }
	    }
	   return props;
	  }
	
	  private
	  Properties doConfigure(java.net.URL configURL) {
	    Properties props = new Properties();
	    LogLog.debug("Reading configuration from URL " + configURL);
	    InputStream istream = null;
	    URLConnection uConn = null;
	    try {
	      uConn = configURL.openConnection();
	      uConn.setUseCaches(false);
	      istream = uConn.getInputStream();
	      props.load(istream);
	    }
	    catch (Exception e) {
	      if (e instanceof InterruptedIOException || e instanceof InterruptedException) {
	          Thread.currentThread().interrupt();
	      }
	      LogLog.error("Could not read configuration file from URL [" + configURL
			   + "].", e);
	      LogLog.error("Ignoring configuration file [" + configURL +"].");
	      return props;
	    }
	    finally {
	        if (istream != null) {
	            try {
	                istream.close();
	            } catch(InterruptedIOException ignore) {
	                Thread.currentThread().interrupt();
	            } catch(IOException ignore) {
	            } catch(RuntimeException ignore) {
	            }
	        }
	    }
	    return props;
	  }
	
}
