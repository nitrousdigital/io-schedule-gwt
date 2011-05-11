package com.nitrous.iosched.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.shared.HandlerRegistration;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionStore;
import com.nitrous.iosched.client.toolbar.RefreshableActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

/**
 * Displays the current active sessions
 * @author Nick
 *
 */
public class NowPlayingView extends AbstractScrollableComposite implements ToolbarEnabledView, Refreshable {
	private RefreshableActivityToolbar toolbar = new RefreshableActivityToolbar("Now Playing");
	private VerticalPanel layout;
	private int width;
	private ActivityController controller;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.NOW_PLAYING);
	public NowPlayingView(int width) {
		this.width = width-20;
		layout = new VerticalPanel();
		layout.setWidth("100%");
		layout.getElement().setId("NowPlaying-scrollpanel");
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.add(layout);
		initWidget(scroll);
		setScrollable(scroll);
	}
	
	public void setController(ActivityController controller) {
		this.controller = controller;
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
	
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd hh:mmaa ZZZ");
	private void loadSessionData(TreeSet<FeedEntry> sorted) {
		onClear();
		// display
		long now = System.currentTimeMillis();
		boolean foundSession = false;
		for (FeedEntry entry : sorted) {
			boolean curMatch = false;
			Date start = FeedEntryComparator.getStartDateTime(entry);
			Date end = FeedEntryComparator.getEndDateTime(entry);
			if (start.getTime() <= now) {
				if (end.getTime() >= now) {
					addSession(entry);
					foundSession = true;
					curMatch = true;
				}
			}
			
			// DEBUG
			if (GWT.isScript() == false) {
				if (!curMatch) {
					GWT.log("X - "
							+ "NOW: "+format.format(new Date(now))
							+ " START:"+ entry.getSessionDate() + " " + format.format(start) 
							+ " END:"+ format.format(end) 
							+ " " + entry.getSessionTitle() 
							+ " is NOT currently playing.");
				} else {
					GWT.log("O - "
							+ "NOW: "+format.format(new Date(now))
							+ " START:"+ entry.getSessionDate() + " " + format.format(start) 
							+ " END:"+ format.format(end) 
							+ " " + entry.getSessionTitle() 
							+ " IS currently playing.");
				}
			}
		}
		
		if (!foundSession) {
			GWT.log("0 sessions found at "+format.format(new Date(now)));
			showMessage("No sessions are currently playing", false);
		} else {			
			refreshScroll();
		}
	}

	
	
	private void showSessionDetail(FeedEntry entry) {
		if (controller != null) {
			controller.showCurrentSessionDetail(entry);
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
