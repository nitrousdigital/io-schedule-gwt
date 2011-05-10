package com.nitrous.iosched.client.view;

import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionTrack;

public interface ActivityController {
	public void showSchedule();
	public void showMap();
	public void showSessionTrackSelector();
	public void showStarred();
	public void showSandboxSelector();
	public void showBulletin();
	public void showSessionTrack(SessionTrack track);
	public void showCompanyPod(CompanyPod sandbox);
	public void showSessionDetail(SessionTrack track, FeedEntry entry);

}
