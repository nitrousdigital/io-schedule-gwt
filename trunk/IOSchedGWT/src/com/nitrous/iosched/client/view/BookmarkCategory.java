package com.nitrous.iosched.client.view;

public enum BookmarkCategory {
	HOME("home"),
	SCHEDULE("schedule"),
	MAP("map"),
	SESSION("sessions"),
	STARRED("starred"),
	SANDBOX("sandbox"),
	BULLETIN("bulletin");
	private String label;
	private BookmarkCategory(String label) {
		this.label = label;
	}
	public String toString() {
		return label;
	}
}
