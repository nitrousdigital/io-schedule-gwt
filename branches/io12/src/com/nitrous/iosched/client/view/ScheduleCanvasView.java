package com.nitrous.iosched.client.view;

import java.util.List;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.model.schedule.Schedule;
import com.nitrous.iosched.client.model.schedule.ScheduleCategory;
import com.nitrous.iosched.client.model.schedule.ScheduleEntry;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class ScheduleCanvasView implements ToolbarEnabledView, IsWidget {
	private static final int QTR_HOUR_PERIOD_HEIGHT = 20;
	private static final int HOUR_PERIOD_HEIGHT = QTR_HOUR_PERIOD_HEIGHT * 4;
	private static final int MIN_HOUR = 7;
	private static final int MAX_HOUR = 22;
	private static final int HOUR_BAR_WIDTH = 70;
	
	private ActivityToolbar toolbar = new ActivityToolbar("Schedule");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SCHEDULE);

	private Canvas canvas;
	private Context2d context;
	private ActivityController controller;

	private Widget widget;

	private int width;
	private int height;
	
	private Schedule schedule;
	private int columnWidth;
	private int columnSpace = 5;
	public ScheduleCanvasView(int width) {
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			this.widget = new Label("Sorry, your browser is not supported");
			return;
		} 
		
		this.widget = new ScrollPanel(canvas);
		this.width = 1000;
		this.columnWidth = width / 3;
		this.height = (HOUR_PERIOD_HEIGHT * ((MAX_HOUR - MIN_HOUR) + 1)) + 10;
		
		this.canvas.setPixelSize(width, height);
		this.canvas.setCoordinateSpaceWidth(width);
		this.canvas.setCoordinateSpaceHeight(height);
		this.context = canvas.getContext2d();
		repaint();
	}
	
	private void repaint() {
		context.clearRect(0,  0, width, height);
		paintTimeline();
		paintData();
	}
	
	public void setSchedule(Schedule schedule) {
		this.schedule = schedule;
	}
	private void paintData() {
//		if (schedule == null) {
//			return;
//		}
//		List<ScheduleEntry> entries = schedule.getEntries(ScheduleCategory.DINING);
//		for (ScheduleEntry entry : entries) {
//			paintEntry(ScheduleCategory.DINING, entry, 0);
//		}
		ScheduleEntry entry = new ScheduleEntry() {

			@Override
			public String getTitle() {
				return "lunch";
			}

			@Override
			public int getStartHour() {
				return 13;
			}

			@Override
			public int getStartMinute() {
				return 0;
			}

			@Override
			public int getEndHour() {
				return 14;
			}

			@Override
			public int getEndMinute() {
				return 0;
			}
		};
		
		paintEntry(ScheduleCategory.DINING, entry, 0);
	}
	private void paintEntry(ScheduleCategory category, ScheduleEntry entry, int column) {
		int startHour = entry.getStartHour();
		int startMinute = entry.getStartMinute();
		int endHour = entry.getEndHour();
		int endMinute = entry.getEndMinute();
		int top = ((startHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((startMinute / 15) * QTR_HOUR_PERIOD_HEIGHT);
		int bottom = ((endHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((endMinute / 15)  * QTR_HOUR_PERIOD_HEIGHT);
		int left = HOUR_BAR_WIDTH + (column * columnWidth) + (column * columnSpace);
		int right = left + columnWidth;
		context.save();
		CanvasGradient grd = context.createLinearGradient(left,  top, left, top + QTR_HOUR_PERIOD_HEIGHT);
		switch (category) {
		case DINING:
			grd.addColorStop(0, "rgba(255,255,255,1)");			
			grd.addColorStop(1, "rgba(255,0,0,1)");
			break;
		}
		context.setFillStyle(grd);
		context.fillRect(left,  top, columnWidth, QTR_HOUR_PERIOD_HEIGHT);
		context.restore();
	}
	
	private void paintTimeline() {
		// hour markers
		context.save();
		
		// hour bar background
		context.setFillStyle(CssColor.make("rgba(128,128,128,0.25)"));
		context.fillRect(0,  0, HOUR_BAR_WIDTH, height);
		
		// time line
		context.setFillStyle("black");
		context.setFont("12pt Calibri");
		context.setTextAlign(TextAlign.RIGHT);
		int y = 0;
		int textX = HOUR_BAR_WIDTH - 5;
		for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
			int minute = 0;
//			for (int minute = 0; minute < 60; minute+= 15) {
				y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((minute / 15) + 1) * QTR_HOUR_PERIOD_HEIGHT;
				String timeStr = toTimeStr(hour, minute);
				context.fillText(timeStr, textX, y);
				y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + HOUR_PERIOD_HEIGHT;
//			}
				
			// hour lines
			context.setStrokeStyle(CssColor.make("rgba(128,128,128,0.5)"));
			context.beginPath();
			context.moveTo(0, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			context.lineTo(width, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			context.closePath();
			context.stroke();
		}
		context.restore();
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
	
	@Override
	public Toolbar getToolbar() {
		return toolbar;
	}

	@Override
	public String getHistoryToken() {
		return bookmark.toString();
	}

	@Override
	public Widget asWidget() {
		return widget;
	}
	
	public void setController(ActivityController controller) {
		this.controller = controller;
	}

}
