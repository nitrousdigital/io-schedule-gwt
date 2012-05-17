package com.nitrous.iosched.client.view;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nitrous.iosched.client.io2011.model.SessionFeedEntry;

public class FeedEntryComparator  implements Comparator<SessionFeedEntry> {
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd hh:mmaa");
	public FeedEntryComparator() {
	}
	public int compare(SessionFeedEntry entry, SessionFeedEntry other) {
		// 1st sort by date/time
		Date entryDate = getStartDateTime(entry);
		Date otherDate = getStartDateTime(other);
		int result = Long.valueOf(entryDate.getTime()).compareTo(Long.valueOf(otherDate.getTime()));
		if (result == 0) {
			// 2nd sort by title
			result = entry.getSessionTitle().compareTo(other.getSessionTitle());
			if (result == 0) {
				// last resort sort by ID
				result = entry.getId().compareTo(other.getId());
			}
		}			
		return result;
	}
	public static Date getStartDateTime(SessionFeedEntry entry) {
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
	public static Date getEndDateTime(SessionFeedEntry entry) {
		Date endTime = entry.getEndDateTimeNative();
		if (endTime == null) {
			// Wednesday May 11
			String date = entry.getSessionDate();
			// 4:15pm-5:15pm
			String time = entry.getSessionTime();
			String dateTime = date + " " + time.substring(time.indexOf("-")+1).trim();
			endTime = format.parse(dateTime);
			entry.setEndDateTimeNative(endTime);
		}
		return endTime;
	}
	
}
