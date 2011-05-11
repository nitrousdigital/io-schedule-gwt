package com.nitrous.iosched.client.model;


public class Configuration {
	private Configuration() {
	}
	// LIVE FEEDS
	public static final String SESSION_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json";
	public static final String SANDBOX_FEED_URL = "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json";
	
	// OFF-LINE MODE
//	public static final String SESSION_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sessions.json";
//	public static final String SANDBOX_FEED_URL = GWT.getHostPageBaseURL() + "/staticfeeds/sandbox.json";

}
