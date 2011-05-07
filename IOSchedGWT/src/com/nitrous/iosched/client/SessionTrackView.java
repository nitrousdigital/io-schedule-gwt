package com.nitrous.iosched.client;

import java.util.Comparator;
import java.util.Date;
import java.util.TreeSet;

import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.Feed;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionData;
import com.nitrous.iosched.client.toolbar.SessionTrackViewToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

/**
 * Displays the sessions for a selected track
 * @author Nick
 *
 */
public class SessionTrackView extends Composite implements ToolbarEnabledWidget, Refreshable {
	private SessionTrackViewToolbar toolbar = new SessionTrackViewToolbar();
	private SessionTrack track;
	private VerticalPanel layout;
	private int width;
	public SessionTrackView(int width) {
		this.width = width-20;
		layout = new VerticalPanel();
		layout.setWidth(this.width+"px");
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
		setTrack(track);
		onRefresh();
	}

	public void setTrack(SessionTrack track) {
		this.track = track;
	}
	
	private void showMessage(String message, boolean isError) {
		onClear();
		Label msg = new Label(message);
		msg.setStyleName(isError ? "sessionErrorMessage" : "sessionMessage");
		layout.add(msg);
	}
	public void onRefresh() {
		showMessage("Loading, Please wait...", false);
		// load all sessions in JSON format
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json");
		try {
			builder.sendRequest(null, new RequestCallback(){
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						showError("Failed to load session data: "+response.getStatusText());
					} else {
						loadSessionData(response.getText());
					}
				}
				public void onError(Request request, Throwable exception) {
					showError("Failed to load session data: "+exception.getMessage());
				}
			});
		} catch (Exception ex) {
			showError("Failed to load session data: "+ex.getMessage());
		}		
	}
	
	private void loadSessionData(String json) {
		onClear();
		SessionData data = SessionData.eval(json);
		Feed feed = data.getFeed();
		JsArray<FeedEntry> entries = feed.getEntries();
		if (entries != null) {
			// sort
			TreeSet<FeedEntry> sorted = new TreeSet<FeedEntry>(feedSorter);			
			for (int i = 0 ; i < entries.length(); i++) {				
				FeedEntry entry = entries.get(i);
				sorted.add(entry);
			}
			// display
			for (FeedEntry entry : sorted) {
				if (track == null || SessionTrack.All.equals(track) || track.toString().equalsIgnoreCase(entry.getSessionTrack())) {
					addSession(entry);
				}
			}
		}
	}

	private static final FeedEntryComparator feedSorter = new FeedEntryComparator();
	private static class FeedEntryComparator implements Comparator<FeedEntry> {
		public int compare(FeedEntry entry, FeedEntry other) {
			// 1st sort by date/time
			Date entryDate = getStartDateTime(entry);
			Date otherDate = getStartDateTime(other);
			int result = Long.valueOf(entryDate.getTime()).compareTo(Long.valueOf(otherDate.getTime()));
			if (result == 0) {
				// 2nd sort by title
				result = entry.getTitle().compareTo(other.getTitle());
				if (result == 0) {
					// last resort sort by ID
					result = entry.getId().compareTo(other.getId());
				}
			}
			return result;
		}
	}
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd hh:mmaa");
	private static Date getStartDateTime(FeedEntry entry) {
		Date startTime = entry.getStartDateTimeNative();
		if (startTime == null) {
			// Wednesday May 11
			String date = entry.getSessionDate();
			// 4:15pm-5:15pm
			String time = entry.getSessionTime();
			String dateTime = date + " " + time;
			dateTime = dateTime.substring(0, dateTime.indexOf("-"));
			startTime = format.parse(dateTime);
			entry.setStartDateTimeNative(startTime);
		}
		return startTime;
	}

	
	private void addSession(FeedEntry entry) {
		VerticalPanel row = new VerticalPanel();
		row.setWidth(this.width+"px");
		row.setStyleName("sessionRow");
		
		Label title = new Label(entry.getSessionTitle());
		title.setStyleName("sessionTitle");
		row.add(title);
		
		Label dateTimePlace = new Label(getDateTimePlace(entry));
		dateTimePlace.setStyleName("sessionLocation");
		row.add(dateTimePlace);
		
		layout.add(row);
	}
	
	private void onClear() {
		this.layout.clear();		
	}
	
	private void showError(String error) {
		showMessage("<font color=\"#d72525\">" + error + "</font>", true);
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
}
