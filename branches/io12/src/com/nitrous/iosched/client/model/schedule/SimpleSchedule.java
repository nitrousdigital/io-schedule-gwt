package com.nitrous.iosched.client.model.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SimpleSchedule implements Schedule {
	private Map<ScheduleCategory, List<ScheduleEntry>> entries;
	private Date date;
	
	public SimpleSchedule() {
		entries = new HashMap<ScheduleCategory, List<ScheduleEntry>>();
	}
	
	public void addEntry(ScheduleCategory category, ScheduleEntry entry) {
		List<ScheduleEntry> list = entries.get(category);
		if (list == null) {
			list = new ArrayList<ScheduleEntry>();
			entries.put(category, list);
		}
		list.add(entry);
	}
	
	public void setDate(Date date) {
		this.date = date;
	}
	
	@Override
	public List<ScheduleEntry> getEntries(ScheduleCategory category) {
		return entries.get(category);
	}

	@Override
	public Date getDate() {
		return date;
	}

}
