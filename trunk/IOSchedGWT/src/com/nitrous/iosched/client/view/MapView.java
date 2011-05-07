package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class MapView extends Composite implements ToolbarEnabledView, Refreshable {
	private ActivityToolbar toolbar = new ActivityToolbar("Map");
	private HTML iframe;
	private static final String html = "<iframe src=\"http://www.google.com/events/io/2011/embed.html#level1\" width=\"100%\" height=\"100%\"></iframe>"; 
	public MapView(int width, int height) {		
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
