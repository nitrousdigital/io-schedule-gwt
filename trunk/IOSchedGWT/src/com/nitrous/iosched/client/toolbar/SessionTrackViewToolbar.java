package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Widget;

public class SessionTrackViewToolbar extends ActivityToolbar {

	public SessionTrackViewToolbar() {
		super("Sessions");
	}

	protected ToolbarText initToolbarLabel(String label) {
		toolbarTitle = new ToolbarText(label, 143);
		return toolbarTitle;
	}
	
	/**
	 * Build the widgets to add to the toolbar
	 * @param toolbarLabel The label for the toolbar
	 * @return The widgets to add to the toolbar
	 */
	protected Widget[] initWidgets(String toolbarLabel) {
		return new Widget[]{
				initHomeButton(),
				initBackButton(),
				initToolbarLabel(toolbarLabel),
				initRefreshButton(),
				initSearchButton()
		};
	}
	
}
