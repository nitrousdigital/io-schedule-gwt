package com.nitrous.iosched.client.view.schedule;

public interface ScheduleViewConfig {	
	static final String TIMELINE_BACKGROUND_COLOR = "rgba(128,128,128,0.25)";
	static final String CORNER_BACKGROUND_COLOR = "rgba(128,128,128,1)";

	static final int QTR_HOUR_PERIOD_HEIGHT = 20;
	static final int HOUR_PERIOD_HEIGHT = QTR_HOUR_PERIOD_HEIGHT * 4;
	static final int MIN_HOUR = 7;
	static final int MAX_HOUR = 22;
	static final int TIMELINE_CANVAS_HEIGHT = (HOUR_PERIOD_HEIGHT * ((MAX_HOUR - MIN_HOUR) + 1)) + 10;
	static final int CATEGORY_HEADER_HEIGHT = 25;
	static final int HOUR_BAR_WIDTH = 70;
	static final int MAX_SESSION_COL_WIDTH = 250;
	static final double GRADIENT_LEN = 0.5D;
	static final int COLUMN_SPACE = 5;

	/** Text & space height in pixels */
	static final int TEXT_LINE_HEIGHT = 20;	
}
