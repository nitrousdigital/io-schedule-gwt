package com.nitrous.iosched.server;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.nitrous.iosched.client.rpc.SessionFeedService;
import com.nitrous.iosched.shared.error.ServiceException;

@SuppressWarnings("serial")
public class ScheduleFeedServiceImpl extends RemoteServiceServlet implements SessionFeedService {
	private static final String LIVE_CODE_LAB_FEED_URL = "https://developers.google.com/events/io/conference/codelab";
	private static final String LIVE_SESSION_FEED_URL = "https://developers.google.com/events/io/conference/session";
//	private static final String SESSION_DETAIL_URL = "https://developers.google.com/events/io/session-details/gooio2012/204/";
	private static final String SESSION_DETAIL_URL = "https://developers.google.com/events/io/session-details/";
	
	public String getSessionDetail() throws ServiceException {
		return getFeedJson(SESSION_DETAIL_URL);
	}
	
	public String getCodeLabFeedJson() throws ServiceException {
		return getFeedJson(LIVE_CODE_LAB_FEED_URL);
	}
	
	public String getSessionFeedJson() throws ServiceException {
		return getFeedJson(LIVE_SESSION_FEED_URL);
	}
	
	private String getFeedJson(String url) throws ServiceException {
		try {
			System.out.println("ScheduleFeedService::getFeedJson("+url+")");
			HttpClient httpclient = new DefaultHttpClient();
			HttpGet httpget = new HttpGet(url);
			HttpResponse response = httpclient.execute(httpget);
			HttpEntity entity = response.getEntity();

			System.out.println("ScheduleFeedService::getFeedJson("+url+") -" +
					" statusCode="+response.getStatusLine().getStatusCode()+
					" status="+response.getStatusLine().getReasonPhrase());
			
	    	StringBuffer buf = new StringBuffer();
			if (entity != null) {
				
			    InputStream instream = entity.getContent();
			    try {
			    	BufferedReader reader = new BufferedReader(new InputStreamReader(instream));
			    	String line = null;
			    	while ((line = reader.readLine()) != null) {
			    		buf.append(line);
			    		buf.append("\n");
			    	}
			    } finally {
			        instream.close();
			    }
			    System.out.println(buf.toString());
			}		
			return buf.toString();
		} catch (Exception ex) {
			throw new ServiceException("Failed to retrieve data feed. Cause: "+ex.getMessage());
		}
	}		
}
