package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.RefreshableSubActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SessionDetailView extends Composite implements ToolbarEnabledView {
	private RefreshableSubActivityToolbar toolbar = new RefreshableSubActivityToolbar("Session Detail");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SESSION);
	
	private VerticalPanel layout;
	private IScroll scroll;
	private Grid infoGrid;
	
	private Label sessionTitle;
	private Label sessionTime;
	private Label sessionRoom;
	private Label sessionSpeakers;
	private Label sessionAbstract;
	private Label[] fields = new Label[]{
			sessionTitle = new Label(),
			sessionTime = new Label(),
			sessionRoom = new Label(),
			sessionSpeakers = new Label(),
			sessionAbstract = new Label()
	};
	public SessionDetailView(int width) {
		width -= 20;
		layout = new VerticalPanel();
		layout.setWidth(width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		initWidget(layout);
		
		infoGrid = new Grid(5,2);
		String[] headers = {
				"Title",
				"Time",
				"Room",
				"Speakers",
				"Abstract"
		}; 
		for (int row = 0 ; row < headers.length; row++) {
			Label header = new Label(headers[row]);
			header.setStyleName("sessionDetailGridHeaderLabel");
			infoGrid.setWidget(row, 0, header);
			
			fields[row].setStyleName("sessionDetailGridText");
			infoGrid.setWidget(row, 1, fields[row]);
		}
		layout.add(infoGrid);
		
		
		layout.getElement().setId("SessionDetailView-scrollpanel");
		scroll = IScroll.applyScroll(layout);
	}
	
	public void showSessionDetail(SessionTrack track, FeedEntry entry) {
		sessionTitle.setText(entry.getSessionTitle());
		sessionTime.setText(entry.getSessionDate()+" "+entry.getSessionTime());
		sessionRoom.setText(entry.getSessionRoom());
		sessionSpeakers.setText(entry.getSessionSpeakers());
		sessionAbstract.setText(entry.getSessionAbstract());
		scroll.refresh();
		
		bookmark.clearStateTokens();
		bookmark.addStateToken(track.getHistoryToken());
		bookmark.addStateToken(entry.getId());
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public String getHistoryToken() {
		return bookmark.toString();
	}

}
