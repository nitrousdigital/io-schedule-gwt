package com.nitrous.iosched.client.view.schedule;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.nitrous.iosched.client.toolbar.ToolbarText;

public class DateToolbarLabel extends ToolbarText {
	private DateTimeFormat format = DateTimeFormat.getFormat("MMMM-dd");
	public DateToolbarLabel() {
		super("", 185);
	}
	
	public void setDate(Date date) {
		setText(format.format(date));
	}
}
