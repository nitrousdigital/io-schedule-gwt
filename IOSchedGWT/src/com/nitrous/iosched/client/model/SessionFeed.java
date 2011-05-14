package com.nitrous.iosched.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The parsed JSON content of a feed loaded from the url http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json
 * @author Nick
 *
 */
public final class SessionFeed extends JavaScriptObject {
	protected SessionFeed() {
	}
	
	/**
	 * Retrieve the entries from this feed
	 * @return the entries from this feed
	 */
	public native JsArray<SessionFeedEntry> getEntries() /*-{
		return this.feed.entry;
	}-*/;
	
	public int getNumResults() {
		return Integer.parseInt(getNumResultsStr());
	}
	public native String getAuthor() /*-{
		return this.feed.author[0].name.$t;
	}-*/;	
	public native String getUpdated() /*-{
		return this.feed.updated.$t; 
	}-*/;
	private native String getNumResultsStr() /*-{
		return this.feed.openSearch$totalResults.$t;
	}-*/;
	
    public static native SessionFeed eval(String json) /*-{
		var ret = eval('(' + json + ')');
		return ret;
	}-*/;
	
}
