package com.nitrous.iosched.client.model;

public enum CompanyPod {
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
	CompanyPod(String label) {
		this.label = label;
	}
	public String toString() {
		return label;
	}

}
