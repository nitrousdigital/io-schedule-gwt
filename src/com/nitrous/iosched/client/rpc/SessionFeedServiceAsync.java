package com.nitrous.iosched.client.rpc;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface SessionFeedServiceAsync {
	public static final SessionFeedServiceAsync INSTANCE = GWT.create(SessionFeedService.class);
	
	void getSessionFeedJson(AsyncCallback<String> callback);

	void getCodeLabFeedJson(AsyncCallback<String> callback);

}
