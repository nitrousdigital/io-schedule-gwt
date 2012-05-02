package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class StarredView extends ResizeComposite implements ToolbarEnabledView {
	private ActivityToolbar toolbar = new ActivityToolbar("Starred");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.STARRED);
	public StarredView(int width, int height) {
		ScrollPanel layout = new ScrollPanel();
		initWidget(layout);
		layout.setStyleName("iconBackground");
		
		Label temp = new Label("Not implemented yet... coming soon!");
		temp.setStyleName("iconText");
		layout.add(temp);
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
