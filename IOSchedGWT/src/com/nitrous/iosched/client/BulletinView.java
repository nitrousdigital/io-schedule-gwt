package com.nitrous.iosched.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

public class BulletinView extends Composite implements ToolbarEnabledWidget, Refreshable {
	private ActivityToolbar toolbar = new ActivityToolbar("Bulletin");
	private HTML iframe;
	private static final String html = "<iframe src=\"http://www.google.com/events/io/2011/mobile_announcements.html\" width=\"100%\" height=\"100%\"></iframe>"; 
	public BulletinView(int width, int height) {		
		iframe = new HTML();
		iframe.setSize(width+"px", height+"px");
		initWidget(iframe);
		onRefresh();
	}

	public void onRefresh() {
		iframe.setHTML(html);
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
}
