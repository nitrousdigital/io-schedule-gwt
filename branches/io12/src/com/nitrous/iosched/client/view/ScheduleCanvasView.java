package com.nitrous.iosched.client.view;

import java.util.ArrayList;
import java.util.Date;
import java.util.Map;
import java.util.TreeSet;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.model.data.EventDataWrapper;
import com.nitrous.iosched.client.model.schedule.ConferenceSchedule;
import com.nitrous.iosched.client.model.schedule.DailySchedule;
import com.nitrous.iosched.client.model.schedule.ScheduleCategory;
import com.nitrous.iosched.client.model.schedule.ScheduleEntry;
import com.nitrous.iosched.client.model.schedule.TimeSlot;
import com.nitrous.iosched.client.model.schedule.TrackSchedule;
import com.nitrous.iosched.client.model.store.SessionStore;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class ScheduleCanvasView implements ToolbarEnabledView, IsWidget, Refreshable {
	private static final int QTR_HOUR_PERIOD_HEIGHT = 20;
	private static final int HOUR_PERIOD_HEIGHT = QTR_HOUR_PERIOD_HEIGHT * 4;
	private static final int MIN_HOUR = 7;
	private static final int MAX_HOUR = 22;
	private static final int HOUR_BAR_WIDTH = 70;
	private static final double GRADIENT_LEN = 0.5D;
	
	private ActivityToolbar toolbar = new ActivityToolbar("Schedule");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SCHEDULE);

	private Canvas canvas;
	private Context2d context;
	private ActivityController controller;

	private Widget widget;

	private int width;
	private int height;
	
	private ConferenceSchedule schedule;
	private Date selectedDate;
	
	private int columnWidth;
	private int columnSpace = 5;
	
	public ScheduleCanvasView(int width) {
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			this.widget = new Label("Sorry, your browser is not supported");
			return;
		} 
		
		this.widget = new ScrollPanel(canvas);
		this.width = width;
		this.columnWidth = (width-(HOUR_BAR_WIDTH + (columnSpace * 3))) / 3;
		this.height = (HOUR_PERIOD_HEIGHT * ((MAX_HOUR - MIN_HOUR) + 1)) + 10;
		
		this.canvas.setPixelSize(width, height);
		this.canvas.setCoordinateSpaceWidth(width);
		this.canvas.setCoordinateSpaceHeight(height);
		this.context = canvas.getContext2d();
		repaint();
		onRefresh();
	}
	
	private void onClear() {
		context.clearRect(0,  0, width, height);
	}
	
	private void showMessage(String message, boolean isError) {
		onClear();
		context.save();
		context.setFont("12pt Calibri");
		context.setTextAlign(TextAlign.LEFT);
		context.setFillStyle(isError ? CssColor.make("red") : CssColor.make("blue"));
		context.fillText(message, 10, 20);
		context.restore();
	}
	
	private void repaint() {
		onClear();
		paintTimeline();
		paintData();
		paintCurrentTimeMarker();
	}
	
	private void paintCurrentTimeMarker() {
		Date now = new Date();
		boolean isCurrentDateShowing = false;
		if (this.selectedDate != null) {
			isCurrentDateShowing = now.getYear() == selectedDate.getYear() 
					&& now.getMonth() == selectedDate.getMonth()
					&& now.getDate() == selectedDate.getDate();									
		}
		
		if (!isCurrentDateShowing) {
			return;
		}
				
		context.save();
		int curHour = now.getHours();
		int curMin = now.getMinutes();
		//TODO: paint time marker
		context.restore();
	}
		
	private void paintData() {
		if (this.selectedDate == null || this.schedule == null) {
			return;
		}
		DailySchedule daily = schedule.getDailySchedule(selectedDate);
		if (daily == null) {
			return;
		}
		
		int column = 0;
		//paintEntry(ScheduleCategory.DINING, entry, column++);
		
		Map<SessionTrack, ArrayList<TrackSchedule>> trackScheds = daily.getTrackSchedules();
		for (Map.Entry<SessionTrack, ArrayList<TrackSchedule>> entry : trackScheds.entrySet()) {
			ArrayList<TrackSchedule> sched = entry.getValue();
			for (TrackSchedule s2 : sched) {
				ArrayList<EventDataWrapper> events = s2.getEvents();
				if (events.size() > 0) {
					for (EventDataWrapper event : events) {
						paintEntry(event, column);
					}
					column++;
				}
			}
		}
		
		//paintEntry(ScheduleCategory.OFFICE_HOURS, entry, column++);
	}
	
	private void paintEntry(EventDataWrapper event, int column) {
		TimeSlot slot = event.getSlot();
		paintEntry(ScheduleCategory.SESSION, slot.getStartHour(), slot.getStartMinute(), slot.getEndHour(), slot.getEndMinute(), event.getData().getTitle(), column); 
	}
	
	private void paintEntry(ScheduleCategory category, ScheduleEntry entry, int column) {
		int startHour = entry.getStartHour();
		int startMinute = entry.getStartMinute();
		int endHour = entry.getEndHour();
		int endMinute = entry.getEndMinute();
		String title = entry.getTitle();
		paintEntry(category, startHour, startMinute, endHour, endMinute, title, column); 				
	}
	
	private void paintEntry(ScheduleCategory  category, int startHour, int startMinute, int endHour, int endMinute, String title, int column) {
		int durationMins = ((endHour * 60) + endMinute) - ((startHour * 60) + startMinute);
		int top = ((startHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((startMinute / 15) * QTR_HOUR_PERIOD_HEIGHT);
		int bottom = ((endHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((endMinute / 15)  * QTR_HOUR_PERIOD_HEIGHT);
		int left = HOUR_BAR_WIDTH + (column * columnWidth) + (column * columnSpace);
		context.save();
		if (durationMins >= 30) {
			// start gradient
			CanvasGradient grd = context.createLinearGradient(left, top, left, top + QTR_HOUR_PERIOD_HEIGHT);
			switch (category) {
			case OFFICE_HOURS:
				grd.addColorStop(0, "rgba(0,128,0,1)");			
				grd.addColorStop(GRADIENT_LEN, "rgba(0,255,0,1)");
				break;
			case DINING:
				grd.addColorStop(0, "rgba(0,128,128,1)");			
				grd.addColorStop(GRADIENT_LEN, "rgba(0,255,255,1)");
				break;
			case SESSION:
				grd.addColorStop(0, "rgba(128,0,0,1)");			
				grd.addColorStop(GRADIENT_LEN, "rgba(255,0,0,1)");
				break;
			}
			context.setFillStyle(grd);
			context.fillRect(left,  top, columnWidth, QTR_HOUR_PERIOD_HEIGHT);
			
			// fill center
			switch (category) {
			case OFFICE_HOURS:
				context.setFillStyle(CssColor.make("rgba(0,255,0,1)"));
				break;
			case DINING:
				context.setFillStyle(CssColor.make("rgba(0,255,255,1)"));
				break;
			case SESSION:
				context.setFillStyle(CssColor.make("rgba(255,0,0,1)"));
				break;
			}
			context.fillRect(left, top + QTR_HOUR_PERIOD_HEIGHT, columnWidth, 1 + bottom - (top + (QTR_HOUR_PERIOD_HEIGHT * 2)));
			
			// end gradient
			CanvasGradient fadeOut = context.createLinearGradient(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, left, bottom);
			switch (category) {
			case OFFICE_HOURS:
				fadeOut.addColorStop(GRADIENT_LEN, "rgba(0,255,0,1)");
				fadeOut.addColorStop(1, "rgba(0,128,0,1)");			
				break;
			case DINING:
				fadeOut.addColorStop(GRADIENT_LEN, "rgba(0,255,255,1)");
				fadeOut.addColorStop(1, "rgba(0,128,128,1)");			
				break;
			case SESSION:
				fadeOut.addColorStop(GRADIENT_LEN, "rgba(255,0,0,1)");
				fadeOut.addColorStop(1, "rgba(128,0,0,1)");			
				break;
			}
			context.setFillStyle(fadeOut);
			context.fillRect(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, columnWidth, QTR_HOUR_PERIOD_HEIGHT - 1);
			
		} else {
			// fill center
			switch (category) {
			case OFFICE_HOURS:
				context.setFillStyle(CssColor.make("rgba(0,255,0,1)"));
				break;
			case DINING:
				context.setFillStyle(CssColor.make("rgba(0,255,255,1)"));
				break;
			case SESSION:
				context.setFillStyle(CssColor.make("rgba(255,0,0,1)"));
				break;
			}
			context.fillRect(left, top, columnWidth, bottom - top);
		}
		
		// content label
		context.setFillStyle("black");
		context.setFont("12pt Calibri");
		context.setTextAlign(TextAlign.CENTER);
		context.fillText(title, 5 + (left + ((columnWidth-10) / 2)), ((bottom - top) / 2) + top, columnWidth-10);
		
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

	@Override
	public void onRefresh() {
		onRefresh(true);
	}
	public void onRefresh(boolean reload) {
		showMessage("Loading, Please wait...", false);
		SessionStore.get().getSessions(new AsyncCallback<ConferenceSchedule>(){
			public void onFailure(Throwable caught) {
				String message = caught.getMessage();
				if (message != null && message.trim().length() > 0) {
					showMessage("Failed to load session data: "+message, true);
				} else {
					showMessage("Failed to load session data", true);
				}						
			}
			public void onSuccess(ConferenceSchedule result) {
				loadSessionData(result);
			}
			
		}, reload);
	}

	private void loadSessionData(ConferenceSchedule schedule) {
		this.schedule = schedule;
		if (this.selectedDate == null) {
			TreeSet<Date> dates = schedule.getDates();
			if (dates != null && dates.size() > 0) {
				this.selectedDate = dates.iterator().next();
			}
		}
		if (this.selectedDate == null) {
			showMessage("No data available", false);
			return;
		}
		repaint();
	}
}
