package com.nitrous.iosched.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.user.client.ui.RootPanel;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IOSchedGWT implements EntryPoint {
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		IOSchedGUI gui = new IOSchedGUI();
		RootPanel.get().add(gui);
	}
}
