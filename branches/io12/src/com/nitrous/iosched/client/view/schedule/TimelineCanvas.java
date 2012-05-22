package com.nitrous.iosched.client.view.schedule;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;

public class TimelineCanvas implements IsWidget, ScheduleViewConfig {
	private AbsolutePanel timelineContainer;
	private Canvas timelineCanvas;
	private Context2d timelineContext;
	private int timelineVPos = 0;

	public TimelineCanvas() {
		this.timelineCanvas = Canvas.createIfSupported();
		this.timelineContext = timelineCanvas.getContext2d();
		this.timelineContainer = new AbsolutePanel();
		this.timelineContainer.setPixelSize(HOUR_BAR_WIDTH, TIMELINE_CANVAS_HEIGHT);
		this.timelineCanvas.setPixelSize(HOUR_BAR_WIDTH, TIMELINE_CANVAS_HEIGHT);
		this.timelineCanvas.setCoordinateSpaceWidth(HOUR_BAR_WIDTH);
		this.timelineCanvas.setCoordinateSpaceHeight(TIMELINE_CANVAS_HEIGHT);
		this.timelineContainer.add(timelineCanvas, 0, 0);
		paintTimeline();
	}
	
	private void paintTimeline() {
		
		// hour markers
		timelineContext.save();
		
		// hour bar background
		timelineContext.setFillStyle(CssColor.make(TIMELINE_BACKGROUND_COLOR));
		timelineContext.fillRect(0,  0, HOUR_BAR_WIDTH, TIMELINE_CANVAS_HEIGHT);
		
		// time line
		timelineContext.setFillStyle("black");
		timelineContext.setFont("12pt Calibri");
		timelineContext.setTextAlign(TextAlign.RIGHT);
		int y = 0;
		int textX = HOUR_BAR_WIDTH - 5;
		for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
			int minute = 0;
			y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((minute / 15) + 1) * QTR_HOUR_PERIOD_HEIGHT;
			String timeStr = toTimeStr(hour, minute);
			timelineContext.fillText(timeStr, textX, y);
			y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + HOUR_PERIOD_HEIGHT;
				
			// hour lines
			timelineContext.setStrokeStyle(CssColor.make("rgba(128,128,128,0.5)"));
			timelineContext.beginPath();
			timelineContext.moveTo(0, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			timelineContext.lineTo(HOUR_BAR_WIDTH, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			timelineContext.closePath();
			timelineContext.stroke();			
		}
		timelineContext.restore();
	}
	
	private static String toTimeStr(int hour, int minute) {
		StringBuffer buf = new StringBuffer();
		String ampm = "am";
		if (hour > 11) {
			ampm = "pm";
		}
		if (hour > 12) {
			hour -= 12;
		}
		if (hour < 10) {
			buf.append(" ");
		}
		buf.append(hour);
		buf.append(":");
		if (minute < 10) {
			buf.append("0");
		}
		buf.append(minute);
		buf.append(" ");
		buf.append(ampm);
		return buf.toString();
	}
	

	/**
	 * Sync vertical position with session scroll position
	 * @param vpos The vertical position of the session scroll
	 */
	public void onSessionScroll(int vpos) {
		if (vpos != this.timelineVPos) {
			this.timelineVPos = vpos;
			this.timelineCanvas.getElement().getStyle().setTop(-timelineVPos, Unit.PX);
		}		
	}
	
	@Override
	public Widget asWidget() {
		return timelineContainer;
	}

}
