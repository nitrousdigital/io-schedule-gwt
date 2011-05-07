package com.nitrous.iosched.client.model;

public enum DayOfWeek {
	Monday("monday", 0),
	Tuesday("tuesday", 1),
	Wednesday("wednesday", 2),
	Thursday("thursday", 3),
	Friday("friday", 4),
	Saturday("saturday", 5),
	Sunday("sunday", 6);
	
	private String label;
	private int index;
	DayOfWeek(String label, int index) {
		this.label = label;	
		this.index = index;
	}
	
	public int getIndex() {
		return index;
	}
	
	/**
	 * Parse the DayOfWeek from the specified date "Wednesday May 11"
	 * @param date The date in the format "Wednesday May 11"
	 * @return The parsed day of week or null
	 */
	public static DayOfWeek getDayOfWeek(String date) {
		for (DayOfWeek day : values()) {
			if (date.toLowerCase().startsWith(day.label)) {
				return day;
			}
		}
		return null;
	}
}
