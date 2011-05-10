package com.nitrous.iosched.client.view;

import java.util.Comparator;
import java.util.Date;

import com.google.gwt.i18n.client.DateTimeFormat;
import com.nitrous.iosched.client.model.FeedEntry;

public class FeedEntryComparator  implements Comparator<FeedEntry> {
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd hh:mmaa");
	public FeedEntryComparator() {
	}
	public int compare(FeedEntry entry, FeedEntry other) {
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
	
}
