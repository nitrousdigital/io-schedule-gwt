package com.nitrous.iosched.client.model;

public enum SessionTrack {
	All("(All sessions)", "all"),
	Android("Android", "android"),
	AppEngine("App Engine", "appengine"),
	Chrome("Chrome", "chrome"),
	Commerce("Commerce", "commerce"),
	DevTools("Developer Tools", "devtools"),
	Geo("Geo", "geo"),			
	GoogleAPIs("Google APIs", "googleapis"),
	GoogleApps("Google Apps", "googleapps"),
	TechTalk("Tech Talk", "techtalk");
	
	private String label;
	private String historyToken;
	SessionTrack(String label, String token) {
		this.label = label;
		this.historyToken = token;
	}
	/**
	 * @return The display label for this session track
	 */
	public String toString() {
		return label;
	}
	public String getHistoryToken() {
		return historyToken;
	}
	public static SessionTrack parseHistoryToken(String token) {
		if (token == null || token.trim().length() == 0) {
			return All;
		}
		for (SessionTrack pod : values()) {
			if (pod.historyToken != null && pod.historyToken.equalsIgnoreCase(token)) {
				return pod;
			}
		}
		return All;
	}
}
