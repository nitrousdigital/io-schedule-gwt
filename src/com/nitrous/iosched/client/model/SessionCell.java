package com.nitrous.iosched.client.model;

/**
 * A bean that is used to describe a block in the schedule view that can be used to map mouse clicks to a time range.
 * @author nick
 *
 */
public class SessionCell {
	private int minRow;
	private int maxRow;
	private int col;
	private long startTime;
	private long endTime;
	/**
	 * 
	 * @param minRow The minimum row in the schedule table
	 * @param maxRow The maximum row in the schedule table
	 * @param col The column in the schedule table
	 * @param startTime the start time of the session block
	 * @param endTime the end time of the session block
	 */
	public SessionCell(int minRow, int maxRow, int col, long startTime, long endTime) {
		this.minRow = minRow;
		this.maxRow = maxRow;
		this.col = col;
		this.startTime= startTime;
		this.endTime = endTime;
	}
	public int getMinRow() {
		return minRow;			
	}
	public int getMaxRow() {
		return maxRow;
	}
	public int getCol() {
		return col;
	}
	public long getStartTime() {
		return startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	
}
