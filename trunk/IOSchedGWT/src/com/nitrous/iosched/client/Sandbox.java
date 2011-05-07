package com.nitrous.iosched.client;

public enum Sandbox {
	All("(All companies)"),
	Accessibility("Accessibility"),
	Android("Android"),
	AppEngine("App Engine"),
	Chrome("Chrome"),
	Commerce("Commerce"),
	DevTools("Developer Tools"),
	GameDev("Game Development"),			
	Geo("Geo"),			
	GoogleApps("Google Apps"),
	GoogleTv("Google TV"),
	YouTube("YouTube");
	
	private String label;
	Sandbox(String label) {
		this.label = label;
	}
	public String toString() {
		return label;
	}

}
