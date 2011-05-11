package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.RefreshableSubActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SessionDetailView extends AbstractScrollableComposite implements ToolbarEnabledView {
	private RefreshableSubActivityToolbar toolbar = new RefreshableSubActivityToolbar("Session Detail");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SESSION);
	
	private VerticalPanel layout;
	private Grid infoGrid;
	
	private HTML sessionTitle;
	private HTML sessionTime;
	private HTML sessionRoom;
	private HTML sessionSpeakers;
	private HTML sessionAbstract;
	private HTML[] fields = new HTML[]{
			sessionTitle = new HTML(),
			sessionTime = new HTML(),
			sessionRoom = new HTML(),
			sessionSpeakers = new HTML(),
			sessionAbstract = new HTML()
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
		setScrollable(layout);
	}
	
	
	/**
	 * Show now-playing session
	 * @param entry The session to show
	 */
	public void showSessionDetail(FeedEntry entry) {
		showSessionDetail(null, entry);
	}
	public void showSessionDetail(SessionTrack track, FeedEntry entry) {
		sessionTitle.setHTML(entry.getSessionTitle());
		sessionTime.setHTML(entry.getSessionDate()+" "+entry.getSessionTime());
		sessionRoom.setHTML(entry.getSessionRoom());
		sessionSpeakers.setHTML(entry.getSessionSpeakers());
		sessionAbstract.setHTML(entry.getSessionAbstract());
		refreshScroll();
		
		bookmark.clearStateTokens();
		if (track != null) {
			bookmark.setCategory(BookmarkCategory.SESSION);
			// navigated from track view instead of now-playing
			bookmark.addStateToken(track.getHistoryToken());
		} else {
			bookmark.setCategory(BookmarkCategory.NOW_PLAYING);
		}
		bookmark.addStateToken(entry.getId());
	}

	public Toolbar getToolbar() {
		return toolbar;
	}

	public String getHistoryToken() {
		return bookmark.toString();
	}

}
