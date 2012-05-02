package com.nitrous.iosched.client.view;

import com.nitrous.iosched.client.model.Configuration;


public class RealtimeView extends AbstractIFrameView {
	private Bookmark bookmark = new Bookmark(BookmarkCategory.REALTIME);
	public RealtimeView(int width, int height) {
		super("Realtime Stream", Configuration.getRealtimeStreamUrl(), width, height);
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
