package com.nitrous.iosched.client.model;

public enum SessionTrack {
	All("(All sessions)", "all"),
	Android("Android", "android"),
	Chrome("Chrome", "chrome"),	
	CloudPlatform("Cloud Platform", "cloudplatform"),
	Commerce("Commerce", "commerce"),
	Entrepreneurship("Entrepreneurship", "entrepreneurship"),
	GoogleAPIs("Google APIs", "googleapis"),
	GoogleDrive("Google Drive", "googledrive"),
	GoogleMaps("Google Maps", "googlemaps"),
	GoogleTv("Google TV", "googletv"),
	GooglePlus("Google+", "googleplus"),
	TechTalk("Tech Talk", "techtalk"),
	YouTube("YouTube", "youtube");
	
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
	
	public static SessionTrack parseTrackName(String name) {
		if (name == null) {
			return null;
		}
		name = name.trim();
		for (SessionTrack track : values()) {
			if (track.label.equalsIgnoreCase(name)) {
				return track;
			}
		}
		return null;
	}
	
	public static SessionTrack parseHistoryToken(String token) {
		if (token == null || token.trim().length() == 0) {
			return All;
		}
		
		for (SessionTrack track : values()) {
			if (track.historyToken != null && track.historyToken.equalsIgnoreCase(token)) {
				return track;
			}
		}
		return All;
	}
}
