package com.nitrous.iosched.client.data;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The session feed loaded from https://developers.google.com/events/io/conference/session
 * or https://developers.google.com/events/io/conference/codelab
 * 
 * @author nick
 *
 */
public final class SessionFeedQueryResult extends JavaScriptObject {
	protected SessionFeedQueryResult() {
	}

	public native SessionData getResult() /*-{
		return this.result[0];
	}-*/;
	
	public native boolean getSuccess() /*-{
		return this.success;
	}-*/;

	public static native SessionData eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
}
