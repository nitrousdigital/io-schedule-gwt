package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.toolbar.RefreshableActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public abstract class AbstractIFrameView extends AbstractScrollableComposite implements ToolbarEnabledView, Refreshable {
	private RefreshableActivityToolbar toolbar;
	private HTML iframe;
	private String html;
	private ScrollPanel layout;
	public AbstractIFrameView(String title, String url, int width, int height) {
		toolbar = new RefreshableActivityToolbar(title);
		html = "<iframe id=\""+title+"-iframe"+"\" src=\"" + url + "\" width=\"100%\" height=\"100%\"></iframe>";
		iframe = new HTML();
		iframe.getElement().setId(title+"-html");
		iframe.setSize("100%", height+"px");
		
		layout = new ScrollPanel();
		layout.setSize("100%", "100%");
		layout.add(iframe);
		layout.getElement().setId(title+"-scrollpanel");
		initWidget(layout);
		setScrollable(layout);
		onRefresh();
	}
	
	public void onRefresh() {
		iframe.setHTML(html);
		refreshScroll();
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
}
