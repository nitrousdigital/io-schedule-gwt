package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarText extends Composite {
	private Label l;
	public ToolbarText(String text) {
		this(text, 185);
	}
	public ToolbarText(String text, int width) {
		l = new Label(text);
		l.setStyleName("toolbarText");
		
		VerticalPanel layout = new VerticalPanel();
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.setStyleName("toolbarFill");
		layout.setHeight("44px");
		layout.add(l);
		layout.setWidth(width+"px");
		initWidget(layout);
	}
	public void setText(String text) {
		l.setText(text);
	}
}
