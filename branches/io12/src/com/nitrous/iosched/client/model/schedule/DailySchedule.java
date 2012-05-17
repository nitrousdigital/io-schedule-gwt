package com.nitrous.iosched.client.model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArrayString;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.model.data.EventDataWrapper;

/**
 * The schedule for a single day
 * 
 * @author nick
 * 
 */
public class DailySchedule {
	private Date date;
	private Map<SessionTrack, ArrayList<TrackSchedule>> schedules;
	/**
	 * Constructor
	 * @param date
	 */
	public DailySchedule(Date date) {
		this.date = date;
		this.schedules = new HashMap<SessionTrack, ArrayList<TrackSchedule>>();
	}
		
	/**
	 * Add an event to the schedule
	 * @param event The event to add
	 */
	public void add(EventDataWrapper event) {
		JsArrayString trackNames = event.getData().getTracks();
		for (int i = 0, len = trackNames.length(); i < len; i++) {
			String trackName = trackNames.get(i);
			SessionTrack track = SessionTrack.parseTrackName(trackName);
			if (track == null) {
				GWT.log("Unrecognized track name: '"+trackName+"'");
				continue;
			}
			
			add(track, event);
		}
	}
	
	private void add(SessionTrack track, EventDataWrapper event) {
		ArrayList<TrackSchedule> trackScheds = schedules.get(track);
		if (trackScheds == null) {
			trackScheds = new ArrayList<TrackSchedule>();
			schedules.put(track,  trackScheds);
		}
		
		// find a track schedule that has an available slot
		TimeSlot slot = event.getSlot();
		TrackSchedule schedule = null;
		for (TrackSchedule sched : trackScheds) {
			boolean available = sched.slotAvaialble(slot);
			if (available) {
				schedule = sched;
				break;
			}			
		}
		
		if (schedule == null) {
			schedule = new TrackSchedule(slot.getStartDate(), track);
			trackScheds.add(schedule);
			GWT.log("Track "+track+" now has "+trackScheds.size()+" concurrent schedules for "+slot.getStartDate());
		}
		
		schedule.add(event);
	}
	
	/**
	 * @return The date of this schedule
	 */
	public Date getDate() {
		return date;
	}

	/**
	 * Returns the schedules for each track on the date associated with this
	 * DailySchedule. Since a track may have overlapping concurrent sessions, a
	 * track may have multiple TrackSchedules returned.
	 * 
	 * @return Returns the schedules for each track on the date associated with
	 *         this DailySchedule. Since a track may have overlapping concurrent
	 *         sessions, a track may have multiple TrackSchedules returned.
	 */
	public Map<SessionTrack, ArrayList<TrackSchedule>> getTrackSchedules() {
		return this.schedules;
	}
	
	/**
	 * 
	 * @param track
	 * @return Returns the schedules for the specified track
	 */
	public ArrayList<TrackSchedule> getSchedules(SessionTrack track) {
		return this.schedules.get(track);
	}

}
