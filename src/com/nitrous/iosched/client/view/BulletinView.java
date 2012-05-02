package com.nitrous.iosched.client.view;


public class BulletinView extends AbstractIFrameView {
	private static final String URL = "http://www.google.com/events/io/2011/mobile_announcements.html";
	private Bookmark bookmark = new Bookmark(BookmarkCategory.BULLETIN);
	public BulletinView(int width, int height) {
		super("Bulletin", URL, width, height);
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
	
}
