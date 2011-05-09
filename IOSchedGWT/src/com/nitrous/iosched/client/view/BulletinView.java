package com.nitrous.iosched.client.view;


public class BulletinView extends AbstractIFrameView {
	private static final String URL = "http://www.google.com/events/io/2011/mobile_announcements.html"; 
	public BulletinView(int width, int height) {
		super("Bulletin", URL, width, height);
	}
}
