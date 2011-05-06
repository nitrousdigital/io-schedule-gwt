package com.nitrous.iosched.client;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.toolbar.SessionTrackViewToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

public class SessionTrackView extends Composite implements ToolbarEnabledWidget {
	private SessionTrackViewToolbar toolbar = new SessionTrackViewToolbar();
	public SessionTrackView(int width) {
		width -= 20;
		VerticalPanel layout = new VerticalPanel();
		layout.setWidth(width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		initWidget(layout);
	}
	
	/**
	 * Load and display the sessions in the specified track
	 * @param track The session track to display
	 */
	public void showSessionTrack(SessionTrack track) {
		toolbar.setTitle(track.toString());
		//TODO: Load and display sessions for track
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
	
}
