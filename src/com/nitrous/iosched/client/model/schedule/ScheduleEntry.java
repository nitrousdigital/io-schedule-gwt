package com.nitrous.iosched.client.model.schedule;

public interface ScheduleEntry {
	public String getTitle();
	public int getStartHour();
	public int getStartMinute();
	public int getEndHour();
	public int getEndMinute();
}
