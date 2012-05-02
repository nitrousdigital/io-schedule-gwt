package com.nitrous.iosched.client.model;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Window;


public class Configuration {
	private Configuration() {
	}
	// LIVE FEEDS
	private static final String LIVE_SESSION_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json";
	private static final String LIVE_SANDBOX_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json";
	//public static final String REALTIME_URL = "http://www.google.com/search?tbs=mbl%3A1&hl=en&source=hp&biw=1170&bih=668&q=%23io2011&btnG=Search";
	private static final String REALTIME_URL = "http://www.google.com/search?tbs=mbl:1&hl=en&source=hp&biw=1170&bih=668&q=#io2011&btnG=Search";
	private static final String MAP_URL = "http://www.google.com/events/io/2011/embed.html#level1";
	
	// OFF-LINE MODE
	private static final String OFFLINE_SESSION_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sessions.json";
	private static final String OFFLINE_SANDBOX_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sandbox.json";

	private static boolean isOffline() {
		return Window.Location.getParameterMap().containsKey("offline");
	}
	public static String getSessionFeed() {
		if (isOffline()) {
			return OFFLINE_SESSION_FEED_URL;
		} else {
			return LIVE_SESSION_FEED_URL;
		}
	}
	public static String getSandboxFeed() {
		if (isOffline()) {
			return OFFLINE_SANDBOX_FEED_URL;
		} else {
			return LIVE_SANDBOX_FEED_URL;
		}
	}
	public static String getRealtimeStreamUrl() {
		return REALTIME_URL;
	}
	public static String getMapUrl() {
		return MAP_URL;
	}
}
