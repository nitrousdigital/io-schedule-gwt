package com.nitrous.iosched.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.ui.RootPanel;
import com.nitrous.iosched.client.view.Bookmark;

/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class IOSchedGWT implements EntryPoint {
	private IOSchedGUI gui;
	/**
	 * This is the entry point method.
	 */
	public void onModuleLoad() {
		gui = new IOSchedGUI();
		RootPanel.get().add(gui);
		
		History.addValueChangeHandler(new ValueChangeHandler<String>(){
			  public void onValueChange(ValueChangeEvent<String> event) {
				  onHistoryChange(event.getValue());
			  }
		});
		String initialToken = History.getToken();
		onHistoryChange(initialToken);
	}
	private void onHistoryChange(String token) {
		if (token == null || token.trim().length() == 0) {
			// initial page load
			History.newItem("home");
		} else {
			Bookmark bookmark = Bookmark.parse(token);
			if (bookmark != null) {
				gui.navigateTo(bookmark);
			}
		}
	}
}
