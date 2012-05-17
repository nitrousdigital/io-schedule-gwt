package com.nitrous.iosched.client.model.schedule;

import java.util.Date;
import java.util.List;

public class ScheduleModel {
	private Schedule[] schedules;
	private int activeScheduleIdx;
	public ScheduleModel() {
		schedules = new Schedule[]{
				new June27()
		};
		activeScheduleIdx = 0;
	}
	
	public Schedule getActiveSchedulePage() {
		return schedules[activeScheduleIdx];
	}
	
	private static class June27 extends SimpleSchedule {
		public June27() {
			Date today = new Date(0);
			today.setYear(2012);
			today.setMonth(5);// june
			today.setDate(27);
			today.setHours(0);
			today.setMinutes(0);
			today.setSeconds(0);
			setDate(today);
			
			this.addEntry(ScheduleCategory.DINING, 
						new SimpleScheduleEntry()
							.setTitle("Lunch")
							.setTime(12, 0, 13, 0));
		}

		@Override
		public List<ScheduleEntry> getEntries(ScheduleCategory category) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Date getDate() {
			// TODO Auto-generated method stub
			return null;
		}
	}
}
