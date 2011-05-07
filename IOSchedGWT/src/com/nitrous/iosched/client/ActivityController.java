package com.nitrous.iosched.client;

public interface ActivityController {
	public void showSchedule();
	public void showMap();
	public void showSessions();
	public void showStarred();
	public void showSandboxSelector();
	public void showBulletin();
	public void showSessionTrack(SessionTrack track);
	public void showSandbox(Sandbox sandbox);
}
