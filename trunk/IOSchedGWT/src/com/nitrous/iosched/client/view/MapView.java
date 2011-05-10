package com.nitrous.iosched.client.view;


public class MapView extends AbstractIFrameView {
	private static final String URL = "http://www.google.com/events/io/2011/embed.html#level1";
	private Bookmark bookmark = new Bookmark(BookmarkCategory.MAP);
	public MapView(int width, int height) {
		super("Map", URL, width, height);
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
