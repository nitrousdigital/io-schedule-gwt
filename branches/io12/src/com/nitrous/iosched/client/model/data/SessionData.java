package com.nitrous.iosched.client.model.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;


public final class SessionData extends JavaScriptObject {
	protected SessionData() {
	}
	
	/**
	 * @return The event slot list for each date
	 */
	public native JsArray<EventSlotList> getEventSlots() /*-{
		return this.event_slots;
	}-*/;
	
	/**
	 * @return codelab or session
	 */
	public native String getEventType() /*-{
		return this.event_type;
	}-*/;
	
	public native JsArray<EventData> getEvents() /*-{
		return this.events;
	}-*/;
	

}
