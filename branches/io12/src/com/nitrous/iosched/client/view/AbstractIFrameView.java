package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Frame;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.toolbar.RefreshableActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public abstract class AbstractIFrameView extends ResizeComposite implements ToolbarEnabledView, Refreshable {
	private RefreshableActivityToolbar toolbar;
	private Frame iframe;
	private String url;
	private ScrollPanel layout;
	public AbstractIFrameView(String title, String url, int width, int height) {
		this.url = url;
		toolbar = new RefreshableActivityToolbar(title);
		iframe = new Frame();		
		iframe.getElement().setId(title+"-html");
		
		layout = new ScrollPanel();
		layout.add(iframe);
		layout.getElement().setId(title+"-scrollpanel");
		initWidget(layout);
		onResize(width, height);
	}
	
	public void onResize(int width, int height) {
		layout.setSize("100%", height+"px");
		iframe.setSize((width-10)+"px", (height-10)+"px");
	}
	
	public void onRefresh() {
		iframe.setUrl(url);
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
}
