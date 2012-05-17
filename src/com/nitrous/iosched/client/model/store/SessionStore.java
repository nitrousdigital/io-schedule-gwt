package com.nitrous.iosched.client.model.store;

import com.google.gwt.core.client.GWT;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitrous.iosched.client.model.Configuration;
import com.nitrous.iosched.client.model.data.SessionData;
import com.nitrous.iosched.client.model.data.SessionFeedQueryResult;
import com.nitrous.iosched.client.model.schedule.ConferenceSchedule;

/**
 * The cache of most recently loaded session data
 * @author nick
 *
 */
public class SessionStore {
	private static SessionStore INSTANCE;

	private ConferenceSchedule conferenceSchedule;
	
	private SessionStore() {
	}
	
	public static SessionStore get() {
		if (INSTANCE == null) {
			INSTANCE = new SessionStore();
		}
		return INSTANCE;
	}
	
	/**
	 * Retrieve the conference schedule
	 * @param callback The callback
	 * @param reload True to force a reload from the server
	 */
	public void getSessions(final AsyncCallback<ConferenceSchedule> callback, boolean reload) {
		if (conferenceSchedule == null || reload) {
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
							SessionFeedQueryResult result = SessionFeedQueryResult.eval(response.getText());
							if (!result.getSuccess()) {
								GWT.log("Result indicated failure");
								callback.onFailure(new Exception("Unexpected response from server"));
								return;
							}
							
							SessionData data = result.getResult();
							
							conferenceSchedule = new ConferenceSchedule(data); 						
							callback.onSuccess(conferenceSchedule);
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
		} else {
			callback.onSuccess(conferenceSchedule);
		}

	}
}
