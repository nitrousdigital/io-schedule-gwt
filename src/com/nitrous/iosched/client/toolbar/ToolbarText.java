package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;

public class ToolbarText extends Composite {
	private Label l;
	public ToolbarText(String text, int width) {
		this(text, width, HasHorizontalAlignment.ALIGN_LEFT);
	}
	public ToolbarText(String text, int width, HasHorizontalAlignment.HorizontalAlignmentConstant align) {
		l = new Label(text);
		l.setStyleName("toolbarText");
		
		VerticalPanel layout = new VerticalPanel();
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		layout.setHorizontalAlignment(align);
		layout.setHeight("44px");
		layout.add(l);
		layout.setWidth(width+"px");
		initWidget(layout);
	}
	public void setText(String text) {
		l.setText(text);
	}
}
