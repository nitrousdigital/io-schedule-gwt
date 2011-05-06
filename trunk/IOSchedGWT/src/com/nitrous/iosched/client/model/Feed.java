package com.nitrous.iosched.client.model;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.JsArray;

/**
 * The parsed JSON content of a feed loaded from the url http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json
 * @author Nick
 *
 */
public final class Feed extends JavaScriptObject {
	protected Feed() {
	}
	
	/**
	 * Retrieve the entries from this feed
	 * @return the entries from this feed
	 */
	public native JsArray<FeedEntry> getEntries() /*-{
		return this.entry;
	}-*/;
	
	public int getNumResults() {
		return Integer.parseInt(getNumResultsStr());
	}
	public native String getAuthor() /*-{
		return this.author[0].name.$t;
	}-*/;	
	public native String getUpdated() /*-{
		return this.updated.$t; 
	}-*/;


	private native String getNumResultsStr() /*-{
		return this.openSearch$totalResults.$t;
	}-*/;
}
