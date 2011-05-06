package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarText extends Composite {
	public ToolbarText(String text) {
		Label l = new Label(text);
		l.setStyleName("toolbarText");
		
		VerticalPanel layout = new VerticalPanel();
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setStyleName("toolbarFill");
		layout.setHeight("44px");
		layout.add(l);
		layout.setWidth("185px");
		initWidget(layout);
	}
}
