package com.nitrous.iosched.client.view;

import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.io2011.model.SessionFeedEntry;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.RefreshableSubActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SessionDetailView extends ResizeComposite implements ToolbarEnabledView {
	private RefreshableSubActivityToolbar toolbar = new RefreshableSubActivityToolbar("Session Detail");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SESSIONS);
	
	private ScrollPanel layout;
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
	/** The selected track from the referring view */
	private SessionTrack referringTrack;
	/** The selected period start time from the referring view */
	private Long referringBlockStartTime;
	/** The selected period end time from the referring view */
	private Long referringBlockEndTime;
	
	public SessionDetailView(int width) {
		width -= 20;
		layout = new ScrollPanel();
		layout.setWidth("100%");
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
	}
	
	
	/**
	 * Show the details of a session selected from the now-showing view
	 * @param entry The session to show
	 */
	public void showSessionDetail(SessionFeedEntry entry) {
		showSessionDetail(null, entry, null, null);
	}
	/**
	 * Show the details of a session selected from a time range view
	 * @param entry The session to show
	 * @param blockStartTime The start of the time range from which the session was selected
	 * @param blockEndTime The end of the time range from which the session was selected
	 */
	public void showSessionDetail(SessionFeedEntry entry, long blockStartTime, long blockEndTime) {
		showSessionDetail(null, entry, blockStartTime, blockEndTime);
	}
	/**
	 * Show the details of a session selected from a track
	 * @param track The track from which the session was selected
	 * @param entry The session to show
	 */
	public void showSessionDetail(SessionTrack track, SessionFeedEntry entry) {
		showSessionDetail(track, entry, null, null);
	}
	/**
	 * Show session details
	 * @param track The track from which the session was selected or null
	 * @param entry The session to show
	 * @param blockStartTime The beginning of the selected time range from which the session was selected or null
	 * @param blockEndTime The end of the selected time range from which the session was selected or null
	 */
	private void showSessionDetail(SessionTrack track, SessionFeedEntry entry, Long blockStartTime, Long blockEndTime) {
		this.referringTrack = track;
		this.referringBlockStartTime = blockStartTime;
		this.referringBlockEndTime = blockEndTime;
		
		sessionTitle.setHTML(entry.getSessionTitle());
		sessionTime.setHTML(entry.getSessionDate()+" "+entry.getSessionTime());
		sessionRoom.setHTML(entry.getSessionRoom());
		sessionSpeakers.setHTML(entry.getSessionSpeakers());
		sessionAbstract.setHTML(entry.getSessionAbstract());
		
		bookmark.clearStateTokens();
		if (track != null) {
			// session was selected on the track view
			bookmark.setCategory(BookmarkCategory.SESSIONS);
			// navigated from track view instead of now-playing
			bookmark.addStateToken(track.getHistoryToken());
		} else if (blockStartTime != null && blockEndTime != null) {
			// session was selected from a time range selected from the schedule view
			bookmark.setCategory(BookmarkCategory.SCHEDULE_SESSIONS);
			bookmark.addStateToken(String.valueOf(blockStartTime));
			bookmark.addStateToken(String.valueOf(blockEndTime));
		} else {
			// session was selected from the now playing view
			bookmark.setCategory(BookmarkCategory.NOW_PLAYING);
		}
		bookmark.addStateToken(entry.getId());
	}

	public SessionTrack getReferringTrack() {
		return referringTrack;
	}
	public Long getReferringSessionBlockStartTime() {
		return referringBlockStartTime;
	}	
	public Long getReferringSessionBlockEndTime() {
		return referringBlockEndTime;
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}

	public String getHistoryToken() {
		return bookmark.toString();
	}

}
