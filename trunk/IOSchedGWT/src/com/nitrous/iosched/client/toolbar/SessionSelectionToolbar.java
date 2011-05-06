package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Widget;

public class SessionSelectionToolbar extends ActivityToolbar {

	public SessionSelectionToolbar() {
		super("Session Tracks");
	}

	/**
	 * Build the widgets to add to the toolbar
	 * @param toolbarLabel The label for the toolbar
	 * @return The widgets to add to the toolbar
	 */
	protected Widget[] initWidgets(String toolbarLabel) {
		return new Widget[]{
				initHomeButton(),
				initToolbarLabel(toolbarLabel, 230),
				initSearchButton()
		};
	}
	
}
