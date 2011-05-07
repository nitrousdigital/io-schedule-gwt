package com.nitrous.iosched.client.model;

public enum SessionTrack {
	All("(All sessions)"),
	Android("Android"),
	AppEngine("App Engine"),
	Chrome("Chrome"),
	Commerce("Commerce"),
	DevTools("Developer Tools"),
	Geo("Geo"),			
	GoogleAPIs("Google APIs"),
	GoogleApps("Google Apps"),
	TechTalk("Tech Talk");
	
	private String label;
	SessionTrack(String label) {
		this.label = label;
	}
	public String toString() {
		return label;
	}
}
