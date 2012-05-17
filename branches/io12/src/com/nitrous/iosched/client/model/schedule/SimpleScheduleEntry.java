package com.nitrous.iosched.client.model.schedule;

public class SimpleScheduleEntry implements ScheduleEntry {
	private String title;
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	
	public SimpleScheduleEntry() {
	}
	
	public SimpleScheduleEntry(String title, int startHour, int startMinute,
			int endHour, int endMinute) {
		super();
		this.title = title;
		setTime(startHour, startMinute, endHour, endMinute);
	}

	public SimpleScheduleEntry setTime(int startHr, int startMin, int endHr, int endMin) {
		this.startHour = startHr;
		this.startMinute = startMin;
		this.endHour = endHr;
		this.endMinute = endMin;
		return this;
	}
	
	public SimpleScheduleEntry setTitle(String title) {
		this.title = title;
		return this;
	}

	public SimpleScheduleEntry setStartHour(int startHour) {
		this.startHour = startHour;
		return this;
	}

	public SimpleScheduleEntry setStartMinute(int startMinute) {
		this.startMinute = startMinute;
		return this;
	}

	public SimpleScheduleEntry setEndHour(int endHour) {
		this.endHour = endHour;
		return this;
	}

	public SimpleScheduleEntry setEndMinute(int endMinute) {
		this.endMinute = endMinute;
		return this;
	}

	@Override
	public String getTitle() {
		return title;
	}

	@Override
	public int getStartHour() {
		return startHour;
	}

	@Override
	public int getStartMinute() {
		return startMinute;
	}

	@Override
	public int getEndHour() {
		return endHour;
	}

	@Override
	public int getEndMinute() {
		return endMinute;
	}

}
