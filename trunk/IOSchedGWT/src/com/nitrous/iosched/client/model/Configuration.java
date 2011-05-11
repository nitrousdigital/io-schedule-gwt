package com.nitrous.iosched.client.model;


public class Configuration {
	private Configuration() {
	}
	// LIVE FEEDS
	public static final String SESSION_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json";
	public static final String SANDBOX_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json";
	//public static final String REALTIME_URL = "http://www.google.com/search?tbs=mbl%3A1&hl=en&source=hp&biw=1170&bih=668&q=%23io2011&btnG=Search";
	public static final String REALTIME_URL = "http://www.google.com/search?tbs=mbl:1&hl=en&source=hp&biw=1170&bih=668&q=#io2011&btnG=Search";
	public static final String MAP_URL = "http://www.google.com/events/io/2011/embed.html#level1";
	
	// OFF-LINE MODE
//	public static final String SESSION_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sessions.json";
//	public static final String SANDBOX_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sandbox.json";

}
