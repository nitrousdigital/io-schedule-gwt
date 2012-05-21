package com.nitrous.iosched.client.rpc;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.nitrous.iosched.shared.error.ServiceException;

@RemoteServiceRelativePath("sessions")
public interface SessionFeedService extends RemoteService {
	public String getSessionFeedJson() throws ServiceException; 
	public String getCodeLabFeedJson() throws ServiceException;

}
