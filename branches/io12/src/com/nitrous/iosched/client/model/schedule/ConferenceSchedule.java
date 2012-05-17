package com.nitrous.iosched.client.model.schedule;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gwt.core.client.JsArray;
import com.nitrous.iosched.client.model.data.EventData;
import com.nitrous.iosched.client.model.data.EventDataWrapper;
import com.nitrous.iosched.client.model.data.SessionData;

/**
 * The schedule for the entire duration of the conference
 * @author nick
 *
 */
public class ConferenceSchedule {
	private Map<Date, DailySchedule> dailySchedules;
	
	public ConferenceSchedule(SessionData data) {
		dailySchedules = new HashMap<Date, DailySchedule>();
		JsArray<EventData> events = data.getEvents();
		for (int i = 0, len = events.length() ; i < len; i++) {
			EventData event = events.get(i);
			EventDataWrapper wrapper = new EventDataWrapper(event);
			TimeSlot slot = wrapper.getSlot();
			Date date = slot.getStartDate();
			
			DailySchedule sched = dailySchedules.get(date);
			if (sched == null) {
				sched = new DailySchedule(date);
				dailySchedules.put(date, sched);
			}
			
			sched.add(wrapper);
		}
	}
}
