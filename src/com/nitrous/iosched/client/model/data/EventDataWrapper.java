package com.nitrous.iosched.client.model.data;

import com.nitrous.iosched.client.model.schedule.TimeSlot;

/**
 * A wrapper for EventData that holds the parsed TimeSlot
 * @author nick
 *
 */
public class EventDataWrapper {
	private EventData data;
	private TimeSlot slot;
	public EventDataWrapper(EventData data) {
		this.data = data;
		this.slot = TimeSlot.parse(data);		
	}
	public EventData getData() {
		return data;
	}
	public TimeSlot getSlot() {
		return slot;
	}
	
	
}
