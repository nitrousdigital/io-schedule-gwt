package com.nitrous.iosched.client.model;

public enum CompanyPod {
	All("(All companies)", "all"),
	Accessibility("Accessibility", "accessibility"),
	Android("Android", "android"),
	AppEngine("App Engine", "appengine"),
	Chrome("Chrome", "chrome"),
	Commerce("Commerce", "commerce"),
	DevTools("Developer Tools", "devtools"),
	GameDev("Game Development", "gamedev"),			
	Geo("Geo", "geo"),			
	GoogleApps("Google Apps", "googleapps"),
	GoogleTv("Google TV", "googletv"),
	YouTube("YouTube", "youtube");
	
	private String label;
	private String historyToken;
	CompanyPod(String label, String token) {
		this.label = label;
		this.historyToken = token;
	}
	/**
	 * @return The display label for this company pod
	 */
	public String toString() {
		return label;
	}
	
	public String getHistoryToken() {
		return historyToken;
	}
	public static CompanyPod parseHistoryToken(String token) {
		if (token == null || token.trim().length() == 0) {
			return All;
		}
		for (CompanyPod pod : values()) {
			if (pod.historyToken != null && pod.historyToken.equalsIgnoreCase(token)) {
				return pod;
			}
		}
		return All;
	}

}
