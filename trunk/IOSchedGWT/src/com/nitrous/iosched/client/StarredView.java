package com.nitrous.iosched.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

public class StarredView extends Composite implements ToolbarEnabledWidget {
	private ActivityToolbar toolbar = new ActivityToolbar("Starred");
	public StarredView(int width, int height) {
		VerticalPanel layout = new VerticalPanel();
		layout.setSize(width+"px", height+"px");
		initWidget(layout);
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		layout.setStyleName("iconBackground");
		
		Label temp = new Label("Not implemented yet... Try the map instead :)");
		temp.setStyleName("iconText");
		layout.add(temp);
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
}
