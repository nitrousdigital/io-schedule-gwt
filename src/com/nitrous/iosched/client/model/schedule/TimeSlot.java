package com.nitrous.iosched.client.model.schedule;

import java.util.Date;

import com.google.gwt.i18n.shared.DateTimeFormat;
import com.nitrous.iosched.client.model.data.EventData;

/**
 * Describes the duration of a session
 * @author nick
 *
 */
public class TimeSlot {
	private int startHour;
	private int startMinute;
	private int endHour;
	private int endMinute;
	private Date startDate;
	private Date endDate;
	private Date startTime;
	private Date endTime;
	private static DateTimeFormat dateFormat = DateTimeFormat.getFormat("yyyy-MM-dd");
	private static DateTimeFormat timeFormat = DateTimeFormat.getFormat("HH:mm");
	
	/**
	 * Parse the TimeSlot from the specified EventData
	 * @param data
	 * @return
	 */
	public static TimeSlot parse(EventData data) {
		return new TimeSlot(data.getStartDate(), data.getStartTime(), data.getEndDate(), data.getEndTime());
	}
	
	/**
	 * Constructor
	 * @param startDate
	 * @param startTime
	 * @param endDate
	 * @param endTime
	 */
	public TimeSlot(String startDate, String startTime, String endDate, String endTime) {
		this.startDate = dateFormat.parse(startDate);
		this.startDate.setHours(0);
		this.startDate.setMinutes(0);
		this.startDate.setSeconds(0);
		
		this.endDate = dateFormat.parse(endDate);
		this.endDate.setHours(0);
		this.endDate.setMinutes(0);
		this.endDate.setSeconds(0);
		
		Date startTm = timeFormat.parse(startTime);
		Date endTm = timeFormat.parse(endTime);
		startHour = startTm.getHours();
		startMinute = startTm.getMinutes();
		endHour = endTm.getHours();
		endMinute = endTm.getMinutes();
		
		this.startTime = new Date(this.startDate.getTime());
		this.startTime.setHours(startHour);
		this.startTime.setMinutes(startMinute);
		
		this.endTime = new Date(this.endDate.getTime());
		this.endTime.setHours(endHour);
		this.endTime.setMinutes(endMinute);
	}
	
	/**
	 * 
	 * @return The start date and time
	 */
	public Date getStartTime() {
		return this.startTime;
	}
	
	/**
	 * 
	 * @return the end date and time
	 */
	public Date getEndTime() {
		return this.endTime;
	}
	
	/**
	 * 
	 * @return the start hour
	 */
	public int getStartHour() {
		return startHour;
	}
	/**
	 * 
	 * @return the start minute
	 */
	public int getStartMinute() {
		return startMinute;
	}
	/**
	 * 
	 * @return the end hour
	 */
	public int getEndHour() {
		return endHour;
	}
	/**
	 * 
	 * @return the end minute
	 */
	public int getEndMinute() {
		return endMinute;
	}
	/**
	 * 
	 * @return the start date (with 0 hours, 0 minutes and 0 seconds)
	 */
	public Date getStartDate() {
		return startDate;
	}
	/**
	 * 
	 * @return the end date (with 0 hours, 0 minutes and 0 seconds)
	 */
	public Date getEndDate() {
		return endDate;
	}
	
}
