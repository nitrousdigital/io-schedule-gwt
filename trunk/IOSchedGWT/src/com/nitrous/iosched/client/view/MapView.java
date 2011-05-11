package com.nitrous.iosched.client.view;

import com.nitrous.iosched.client.model.Configuration;


public class MapView extends AbstractIFrameView {
	private Bookmark bookmark = new Bookmark(BookmarkCategory.MAP);
	public MapView(int width, int height) {
		super("Map", Configuration.MAP_URL, width, height);
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
