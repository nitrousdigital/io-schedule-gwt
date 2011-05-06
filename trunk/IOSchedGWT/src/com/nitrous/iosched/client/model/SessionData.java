package com.nitrous.iosched.client.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The raw JSON returned from the URL http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json
 * @author Nick
 *
 */
public final class SessionData extends JavaScriptObject {
	protected SessionData() {
	}
	
	/**
	 * @return The parsed JSON content from this feed
	 */
	public native Feed getFeed() /*-{
		return this.feed;
	}-*/;
	
    public static native SessionData eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
	
}
