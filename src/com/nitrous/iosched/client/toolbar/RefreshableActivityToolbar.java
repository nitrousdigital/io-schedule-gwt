package com.nitrous.iosched.client.toolbar;

import com.google.gwt.user.client.ui.Widget;

/**
 * A toolbar for sub-activities that has the following buttons:
 * home, refresh and search
 * 
 * @author nick
 *
 */
public class RefreshableActivityToolbar extends ActivityToolbar {
	public RefreshableActivityToolbar(String label) {
		super(label);
	}
	
	/**
	 * Build the widgets to add to the toolbar
	 * @param toolbarLabel The label for the toolbar
	 * @return The widgets to add to the toolbar
	 */
	protected Widget[] initWidgets(String toolbarLabel) {
		return new Widget[]{
				initHomeButton(),
				initToolbarLabel(toolbarLabel),
				initRefreshButton(),
				initSearchButton()
		};
	}
	
}
