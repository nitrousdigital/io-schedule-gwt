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
	/**
	 * Display the detail view for a session
	 * @param track The track to be displayed when BACK is clicked
	 * @param entry The session entry to show
	 */
	public void showSessionDetail(SessionTrack track, FeedEntry entry);
	/**
	 * Display the detail view for a session that is currently playing.
	 * BACK navigation should return to the now-playing view.
	 * @param entry The entry to display
	 */
	public void showCurrentSessionDetail(FeedEntry entry);
	public void showNowPlaying();

}
