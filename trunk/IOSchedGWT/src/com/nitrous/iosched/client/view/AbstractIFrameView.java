package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.toolbar.RefreshableActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public abstract class AbstractIFrameView extends Composite implements ToolbarEnabledView, Refreshable {
	private RefreshableActivityToolbar toolbar;
	private HTML iframe;
	private IScroll scroll;
	private String html;
	public AbstractIFrameView(String title, String url, int width, int height) {
		toolbar = new RefreshableActivityToolbar(title);
		html = "<iframe id=\""+title+"-iframe"+"\" src=\"" + url + "\" width=\"100%\" height=\"100%\"></iframe>";
		iframe = new HTML();
		iframe.getElement().setId(title+"-html");
		iframe.setSize("100%", height+"px");
		
		VerticalPanel layout = new VerticalPanel();
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setSize(width+"px", height+"px");
		layout.add(iframe);
		layout.getElement().setId(title+"-scrollpanel");
		initWidget(layout);
		
		scroll = IScroll.applyScroll(layout);
		onRefresh();
	}

	public void onRefresh() {
		iframe.setHTML(html);
		scroll.refresh();
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
}
