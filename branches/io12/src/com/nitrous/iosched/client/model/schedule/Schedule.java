package com.nitrous.iosched.client.model.schedule;

import java.util.List;

public interface Schedule {
	public List<ScheduleEntry> getEntries(ScheduleCategory category);
}
