package com.nitrous.iosched.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionStore;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.RefreshableSubActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

/**
 * Displays the sessions for a selected track or time range
 * @author Nick
 *
 */
public class SessionListView extends AbstractScrollableComposite implements ToolbarEnabledView, Refreshable {
	private RefreshableSubActivityToolbar toolbar = new RefreshableSubActivityToolbar("Sessions");
	
	// track for session track mode
	private SessionTrack track;
	
	// dare range for session block mode
	private Long start;
	private Long end;
	
	private VerticalPanel layout;
	private int width;
	private ActivityController controller;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SESSIONS);
	public SessionListView(int width) {
		this.width = width-20;
		layout = new VerticalPanel();
		layout.setWidth("100%");
		layout.getElement().setId("SessionTrackView-scrollpanel");
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.add(layout);
		initWidget(scroll);
		setScrollable(layout);
	}
	
	public void setController(ActivityController controller) {
		this.controller = controller;
	}

	/**
	 * Load and display the sessions in the specified track
	 * @param track The session track to display
	 */
	public void showSessionTrack(SessionTrack track) {
		setTrack(track);
		onRefresh(false);
	}

	public void showSessionTimeRange(long start, long end) {
		this.toolbar.setTitle("Sessions");
		this.track = null;
		this.start = start;
		this.end = end;
		this.bookmark.setCategory(BookmarkCategory.SCHEDULE_SESSIONS);
		this.bookmark.clearStateTokens();
		this.bookmark.addStateToken(String.valueOf(start));
		this.bookmark.addStateToken(String.valueOf(end));
		onRefresh(false);
	}
	
	public SessionTrack getTrack() {
		return track;
	}
	public void setTrack(SessionTrack track) {
		this.start = null;
		this.end = null;
		this.track = track;
		this.toolbar.setTitle(track.toString());
		this.bookmark.setCategory(BookmarkCategory.SESSIONS);
		this.bookmark.setStateToken(track.getHistoryToken());
	}
	
	private void showMessage(String message, boolean isError) {
		onClear();
		Label msg = new Label(message);
		msg.setStyleName(isError ? "sessionErrorMessage" : "sessionMessage");
		layout.add(msg);
		refreshScroll();
	}
	
	public void onRefresh() {
		onRefresh(true);
	}
	public void onRefresh(boolean reload) {
		showMessage("Loading, Please wait...", false);
		SessionStore.get().getSessions(new AsyncCallback<TreeSet<FeedEntry>>(){
			public void onFailure(Throwable caught) {
				String message = caught.getMessage();
				if (message != null && message.trim().length() > 0) {
					showMessage("Failed to load session data: "+message, true);
				} else {
					showMessage("Failed to load session data", true);
				}						

			}
			public void onSuccess(TreeSet<FeedEntry> result) {
				loadSessionData(result);
			}
			
		}, reload);
	}
	
	private void loadSessionData(TreeSet<FeedEntry> sorted) {
		onClear();
		// display
		int displayedSessions = 0;
		for (FeedEntry entry : sorted) {
			if (start != null) {
				// date range mode
				Date sessionStart = FeedEntryComparator.getStartDateTime(entry);
				long sessionStartMs = sessionStart.getTime();
				if (sessionStartMs >= start && sessionStartMs <= end) {
					addSession(entry);
					displayedSessions++;
				}
			} else {
				// session track mode
				if (track == null || SessionTrack.All.equals(track) || track.toString().equalsIgnoreCase(entry.getSessionTrack())) {
					addSession(entry);
					displayedSessions++;
				}
			}
		}
		if (displayedSessions == 0) {
			showMessage("0 sessions found", false);
		} else {
			refreshScroll();
		}
	}

	
	
	private void showSessionDetail(FeedEntry entry) {
		if (controller != null) {
			if (this.start == null) {
				controller.showSessionDetail(this.track, entry);
			} else {
				controller.showSessionDetail(entry, this.start, this.end);
			}
		}
	}
	
	private ArrayList<HandlerRegistration> sessionClickHandlers = new ArrayList<HandlerRegistration>();
	private void addSession(final FeedEntry entry) {
		VerticalPanel row = new VerticalPanel();
		row.setWidth(this.width+"px");
		row.setStyleName("sessionRow");		
		
		Label title = new Label(entry.getSessionTitle());
		title.setStyleName("sessionTitle");
		title.setWidth("100%");
		HandlerRegistration reg = title.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				showSessionDetail(entry);
			}
		});
		sessionClickHandlers.add(reg);
		row.add(title);
		
		Label dateTimePlace = new Label(getDateTimePlace(entry));
		dateTimePlace.setStyleName("sessionLocation");
		dateTimePlace.setWidth("100%");
		reg = dateTimePlace.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				showSessionDetail(entry);
			}
		});
		sessionClickHandlers.add(reg);
		row.add(dateTimePlace);
		
		layout.add(row);
	}
	
	private void onClear() {
		for (HandlerRegistration reg : sessionClickHandlers) {
			reg.removeHandler();
		}
		sessionClickHandlers.clear();
		this.layout.clear();	
		refreshScroll();
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
	
	private static String getDateTimePlace(FeedEntry entry) {
		StringBuilder buf = new StringBuilder();
		buf.append(entry.getSessionTime());
		buf.append(", ");
		String date = entry.getSessionDate().toLowerCase();;
		if (date.startsWith("tuesday")) {
			buf.append("Tue");			
		} else if (date.startsWith("wednesday")) {
			buf.append("Wed");
		} else if (date.startsWith("thursday")) {
			buf.append("Thr");
		} else if (date.startsWith("friday")) {
			buf.append("Fri");
		} else if (date.startsWith("saturday")) {
			buf.append("Sat");
		} else if (date.startsWith("sunday")) {
			buf.append("Sun");
		} else if (date.startsWith("monday")) {
			buf.append("Mon");
		}
		buf.append(" in Room ");
		buf.append(entry.getSessionRoom());
		return buf.toString();
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
