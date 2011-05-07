package com.nitrous.iosched.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The parsed JSON content of a feed loaded from the url http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json
 * @author Nick
 *
 */
public final class SandboxFeed extends JavaScriptObject {
	protected SandboxFeed() {
	}
	
	/**
	 * Retrieve the entries from this feed
	 * @return the entries from this feed
	 */
	public native JsArray<SandboxFeedEntry> getEntries() /*-{
		return this.feed.entry;
	}-*/;
	
	public native String getUpdated() /*-{
		return this.feed.updated.$t; 
	}-*/;
	
    public static native SandboxFeed eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
	
}
