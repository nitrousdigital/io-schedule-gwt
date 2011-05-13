package com.nitrous.iosched.client.model;

import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitrous.iosched.client.view.FeedEntryComparator;

/**
 * The cache of the most recently loaded list of session data
 * @author nick
 *
 */
public class SessionStore {
	private static SessionStore INSTANCE;
	private static final FeedEntryComparator feedSorter = new FeedEntryComparator();
	private TreeSet<FeedEntry> sessions = new TreeSet<FeedEntry>();
	private SessionStore() {
	}
	public static SessionStore get() {
		if (INSTANCE == null) {
			INSTANCE = new SessionStore();
		}
		return INSTANCE;
	}
	
	/**
	 * Retrieve the FeedEntry for the specified session ID
	 * @param sessionId The ID of the session
	 * @param callback The callback
	 */
	public void getSession(final String sessionId, final AsyncCallback<FeedEntry> callback) {
		getSessions(new AsyncCallback<TreeSet<FeedEntry>>(){
			public void onFailure(Throwable caught) {
				callback.onFailure(caught);
			}
			public void onSuccess(TreeSet<FeedEntry> result) {
				if (result != null) {
					for (FeedEntry entry : result) {
						if (entry.getId().equals(sessionId)) {
							callback.onSuccess(entry);
							return;
						}
					}
				}
				callback.onFailure(new Exception("Session not found"));
			}
		}, false);
	}
	
	/**
	 * Retrieve the session list
	 * @param callback The callback
	 * @param reload True to force a reload from the server
	 */
	public void getSessions(final AsyncCallback<TreeSet<FeedEntry>> callback, boolean reload) {
		if (sessions.size() > 0 && reload == false) {
			callback.onSuccess(sessions);
		} else {
			RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Configuration.getSessionFeed());
			try {
				builder.sendRequest(null, new RequestCallback(){
					public void onResponseReceived(Request request, Response response) {
						if (response.getStatusCode() != Response.SC_OK) {
							String body = null;
							try {
								body = response.getStatusText();
							} catch (Throwable t) {
								GWT.log("Failed to load session data", t);
								callback.onFailure(t);
								return;
							}
							if (body != null && body.trim().length() > 0) {
								callback.onFailure(new Exception(body));
								return;
							} else {
								callback.onFailure(new Exception("Unexpected response from server"));
								return;
							}						
						} else {
							SessionData data = SessionData.eval(response.getText());
							Feed feed = data.getFeed();
							JsArray<FeedEntry> entries = feed.getEntries();
							if (entries != null) {
								// sort
								TreeSet<FeedEntry> sorted = new TreeSet<FeedEntry>(feedSorter);			
								for (int i = 0 ; i < entries.length(); i++) {				
									FeedEntry entry = entries.get(i);
									sorted.add(entry);
								}
								sessions = sorted;
								callback.onSuccess(sorted);
							}
						}
					}
					public void onError(Request request, Throwable exception) {
						GWT.log("Failed to load session data", exception);
						String message = exception.getMessage();
						if (message != null && message.trim().length() > 0) {
							callback.onFailure(new Exception(message));
						} else {
							callback.onFailure(new Exception("Unexpected response from server"));
						}
					}
				});
			} catch (Exception ex) {
				GWT.log("Failed to load session data", ex);
				String message = ex.getMessage();
				if (message != null && message.trim().length() > 0) {
					callback.onFailure(new Exception(message));
				} else {
					callback.onFailure(new Exception("Unexpected response from server"));
				}
			}		
		}
	}
}
