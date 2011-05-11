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
	/**
	 * Display all sessions in the specified track
	 * @param track The track to display
	 */
	public void showSessionTrack(SessionTrack track);
	/**
	 * Display all sessions within the specified time range
	 * @param startTime The start time
	 * @param endTime The end time
	 */
	public void showSessionTimeRange(long startTime, long endTime);
	/**
	 * Show all companies in the specified pod
	 * @param pod The pod to display
	 */
	public void showCompanyPod(CompanyPod pod);
	/**
	 * Display the detail view for a session
	 * @param track The track to be displayed when BACK is clicked
	 * @param entry The session entry to show
	 */
	public void showSessionDetail(SessionTrack track, FeedEntry entry);
	/**
	 * Show the details of a session selected from a time range view
	 * @param entry The session to show
	 * @param blockStartTime The start of the time range from which the session was selected
	 * @param blockEndTime The end of the time range from which the session was selected
	 */
	public void showSessionDetail(FeedEntry entry, long blockStartTime, long blockEndTime);

	/**
	 * Display the detail view for a session that is currently playing.
	 * BACK navigation should return to the now-playing view.
	 * @param entry The entry to display
	 */
	public void showCurrentSessionDetail(FeedEntry entry);
	public void showNowPlaying();

}
