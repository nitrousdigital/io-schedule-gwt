package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Widget;

public interface Toolbar {
	public Widget getUI();
	public void setController(ToolbarController controller);
	public ToolbarController getController();

}
