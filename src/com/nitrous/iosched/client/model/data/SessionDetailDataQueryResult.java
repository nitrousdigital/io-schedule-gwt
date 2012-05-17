package com.nitrous.iosched.client.model.data;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The session details returned by querying a session detail url such as: https://developers.google.com/events/io/session-details/gooio2012/204/
 * @author nick
 *
 */
public final class SessionDetailDataQueryResult extends JavaScriptObject {
	protected SessionDetailDataQueryResult() {
	}

	public native SessionData getSessionData() /*-{
		return this.session_data;
	}-*/;
	
	public native String getSessionId() /*-{
		return this.session_id;
	}-*/;
	
	public native int getAttending() /*-{
		return this.result.attending;
	}-*/;
	
	public native JsArray<SpeakerData> getSpeakers() /*-{
		return this.result.speakers;
	}-*/;
	
	public native boolean getSuccess() /*-{
		return this.success;
	}-*/;

	public static native SessionDetailDataQueryResult eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;

}
