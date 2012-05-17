package com.nitrous.iosched.client.model.schedule;

import java.util.ArrayList;
import java.util.Date;

import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.model.data.EventDataWrapper;

/**
 * The daily schedule for a single track
 * @author nick
 *
 */
public class TrackSchedule {
	private Date date;
	private SessionTrack track;
	private ArrayList<EventDataWrapper> events;
	
	public TrackSchedule(Date date, SessionTrack track) {
		super();
		this.date = date;
		this.track = track;
		this.events = new ArrayList<EventDataWrapper>();
	}

	public boolean slotAvaialble(TimeSlot slot) {
		long requestStartMillis = slot.getStartTime().getTime();
		long requestEndMillis = slot.getEndTime().getTime();
		for (EventDataWrapper wrapper : events) {
			TimeSlot usedSlot = wrapper.getSlot();
			long usedStartMillis = usedSlot.getStartTime().getTime();
			long usedEndMillis = usedSlot.getEndTime().getTime();
			
			// Slot is NOT available if this condition is FALSE:
			// if the requested slot ends before this slot starts
			// or the requested slot starts after this slot ends
			if (!(requestEndMillis <= usedStartMillis ||
					requestStartMillis >= usedEndMillis)) {
				// overlap detected
				return false;
			}
		}
		// slot is available
		return true;
	}
	
	
	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public SessionTrack getTrack() {
		return track;
	}

	public void add(EventDataWrapper event) {
		this.events.add(event);
	}
	
	public ArrayList<EventDataWrapper> getEvents() {
		return events;
	}

}
