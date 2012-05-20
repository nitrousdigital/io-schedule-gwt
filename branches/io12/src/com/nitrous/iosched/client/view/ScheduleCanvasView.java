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
import com.google.gwt.dom.client.Style.Overflow;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
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

	private Canvas sessionCanvas;
	private Context2d sessionContext;
	
	private Canvas timelineCanvas;
	private Context2d timelineContext;
	
	private ActivityController controller;

	private DockLayoutPanel layout;

	private int sessionCanvasWidth;
	private int sessionCanvasHeight;
	
	private ConferenceSchedule schedule;
	private Date selectedDate;
	
	private int columnWidth;
	private int columnSpace = 5;
	
	private ScrollPanel sessionScroll;
	
	public ScheduleCanvasView() {
		layout = new DockLayoutPanel(Unit.PX);
		
		this.sessionCanvas = Canvas.createIfSupported();
		if (this.sessionCanvas == null) {
			this.layout.add(new Label("Sorry, your browser is not supported"));
			return;
		} 
		this.sessionContext = sessionCanvas.getContext2d();
		this.timelineCanvas = Canvas.createIfSupported();
		this.timelineContext = timelineCanvas.getContext2d();
				
		this.sessionScroll = new ScrollPanel(sessionCanvas);
		this.sessionScroll.addScrollHandler(new com.google.gwt.event.dom.client.ScrollHandler(){
			@Override
			public void onScroll(ScrollEvent event) {
				onSessionScroll();
			}
		});
		
		layout.addWest(timelineCanvas, HOUR_BAR_WIDTH);
		layout.add(sessionScroll);

		onResize();
		onRefresh();
		Window.addResizeHandler(new ResizeHandler(){
			@Override
			public void onResize(ResizeEvent event) {
				ScheduleCanvasView.this.onResize();
			}
		});
	}
		
	private void onSessionScroll() {
		// account for current session scroll vertical position
		int vpos = sessionScroll.getVerticalScrollPosition();
		if (vpos != this.timelineVPos) {
			this.timelineVPos = vpos;
			repaintTimeline();
		}		
	}
	
	private void onResize() {
		int winWidth = Window.getClientWidth();
		int winHeight = Window.getClientHeight();
		this.layout.setPixelSize(winWidth, winHeight);
		
		int trackColCount = getTrackColCount();
		
		this.columnWidth = (winWidth-(HOUR_BAR_WIDTH + (columnSpace * 2))) / 2;
		this.columnWidth = Math.min(columnWidth, 300);
		this.sessionCanvasWidth = (trackColCount * columnWidth) + (columnSpace * (trackColCount > 0 ? trackColCount - 1 : 0));
		this.sessionCanvasHeight = (HOUR_PERIOD_HEIGHT * ((MAX_HOUR - MIN_HOUR) + 1)) + 10;
		
		
		this.sessionCanvas.setPixelSize(sessionCanvasWidth, sessionCanvasHeight);
		this.sessionCanvas.setCoordinateSpaceWidth(sessionCanvasWidth);
		this.sessionCanvas.setCoordinateSpaceHeight(sessionCanvasHeight);
		
		this.timelineCanvas.setPixelSize(HOUR_BAR_WIDTH, sessionCanvasHeight);
		this.timelineCanvas.setCoordinateSpaceWidth(HOUR_BAR_WIDTH);
		this.timelineCanvas.setCoordinateSpaceHeight(sessionCanvasHeight);
		repaint();
	}
	
	private void onClear() {
		sessionContext.clearRect(0,  0, sessionCanvasWidth, sessionCanvasHeight);
		timelineContext.clearRect(0,  0, HOUR_BAR_WIDTH, sessionCanvasHeight);
	}
	
	private void showMessage(String message, boolean isError) {
		onClear();
		sessionContext.save();
		sessionContext.setFont("12pt Calibri");
		sessionContext.setTextAlign(TextAlign.LEFT);
		sessionContext.setFillStyle(isError ? CssColor.make("red") : CssColor.make("blue"));
		sessionContext.fillText(message, 10, 20);
		sessionContext.restore();
	}
	
	private void repaint() {
		onClear();
		paintTimeline();
		paintSessionHourLines();
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
				
		sessionContext.save();
		int curHour = now.getHours();
		int curMin = now.getMinutes();
		//TODO: paint time marker
		sessionContext.restore();
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
				SessionTrack track = s2.getTrack();
				ArrayList<EventDataWrapper> events = s2.getEvents();
				if (events.size() > 0) {
					for (EventDataWrapper event : events) {
						paintEntry(track, event, column);
					}
					column++;
				}
			}
		}
		
		//paintEntry(ScheduleCategory.OFFICE_HOURS, entry, column++);
	}
	
	private void paintEntry(SessionTrack track, EventDataWrapper event, int column) {
		TimeSlot slot = event.getSlot();
		paintEntry(track, 
				slot.getStartHour(), 
				slot.getStartMinute(), 
				slot.getEndHour(), 
				slot.getEndMinute(), 
				event.getData().getTitle(), 
				column); 
	}
	
	private void paintEntry(SessionTrack track, int startHour, int startMinute, int endHour, int endMinute, String title, int column) {
		int durationMins = ((endHour * 60) + endMinute) - ((startHour * 60) + startMinute);
		int top = ((startHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((startMinute / 15) * QTR_HOUR_PERIOD_HEIGHT);
		int bottom = ((endHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((endMinute / 15)  * QTR_HOUR_PERIOD_HEIGHT);
		int left = (column * columnWidth) + (column * columnSpace);
		sessionContext.save();
		String startColor = SessionFillStyle.getStartGradientColor(track);
		String endColor = SessionFillStyle.getEndGradientColor(track);
		String textColor = SessionFillStyle.getTextColor(track);
		if (durationMins >= 30) {
			// start gradient
			CanvasGradient grd = sessionContext.createLinearGradient(left, top, left, top + QTR_HOUR_PERIOD_HEIGHT);
			grd.addColorStop(0, startColor);			
			grd.addColorStop(GRADIENT_LEN, endColor);
			sessionContext.setFillStyle(grd);
			sessionContext.fillRect(left,  top, columnWidth, QTR_HOUR_PERIOD_HEIGHT);
			
			// fill center
			sessionContext.setFillStyle(endColor);
			sessionContext.fillRect(left, top + QTR_HOUR_PERIOD_HEIGHT, columnWidth, 1 + bottom - (top + (QTR_HOUR_PERIOD_HEIGHT * 2)));
			
			// end gradient
			CanvasGradient fadeOut = sessionContext.createLinearGradient(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, left, bottom);
			fadeOut.addColorStop(GRADIENT_LEN, endColor);
			fadeOut.addColorStop(1, startColor);			
			sessionContext.setFillStyle(fadeOut);
			sessionContext.fillRect(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, columnWidth, QTR_HOUR_PERIOD_HEIGHT - 1);
			
		} else {
			// fill center
			sessionContext.setFillStyle(endColor);
			sessionContext.fillRect(left, top, columnWidth, bottom - top);
		}
		
		// content label
		sessionContext.setFillStyle(textColor);
		sessionContext.setFont("12pt Calibri");
		sessionContext.setTextAlign(TextAlign.CENTER);
		sessionContext.fillText(title, 5 + (left + ((columnWidth-10) / 2)), ((bottom - top) / 2) + top, columnWidth-10);
		
		sessionContext.restore();
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
		int left = (column * columnWidth) + (column * columnSpace);
		sessionContext.save();
		if (durationMins >= 30) {
			// start gradient
			CanvasGradient grd = sessionContext.createLinearGradient(left, top, left, top + QTR_HOUR_PERIOD_HEIGHT);
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
			sessionContext.setFillStyle(grd);
			sessionContext.fillRect(left,  top, columnWidth, QTR_HOUR_PERIOD_HEIGHT);
			
			// fill center
			switch (category) {
			case OFFICE_HOURS:
				sessionContext.setFillStyle(CssColor.make("rgba(0,255,0,1)"));
				break;
			case DINING:
				sessionContext.setFillStyle(CssColor.make("rgba(0,255,255,1)"));
				break;
			case SESSION:
				sessionContext.setFillStyle(CssColor.make("rgba(255,0,0,1)"));
				break;
			}
			sessionContext.fillRect(left, top + QTR_HOUR_PERIOD_HEIGHT, columnWidth, 1 + bottom - (top + (QTR_HOUR_PERIOD_HEIGHT * 2)));
			
			// end gradient
			CanvasGradient fadeOut = sessionContext.createLinearGradient(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, left, bottom);
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
			sessionContext.setFillStyle(fadeOut);
			sessionContext.fillRect(left, bottom + 1 - QTR_HOUR_PERIOD_HEIGHT, columnWidth, QTR_HOUR_PERIOD_HEIGHT - 1);
			
		} else {
			// fill center
			switch (category) {
			case OFFICE_HOURS:
				sessionContext.setFillStyle(CssColor.make("rgba(0,255,0,1)"));
				break;
			case DINING:
				sessionContext.setFillStyle(CssColor.make("rgba(0,255,255,1)"));
				break;
			case SESSION:
				sessionContext.setFillStyle(CssColor.make("rgba(255,0,0,1)"));
				break;
			}
			sessionContext.fillRect(left, top, columnWidth, bottom - top);
		}
		
		// content label
		sessionContext.setFillStyle("black");
		sessionContext.setFont("12pt Calibri");
		sessionContext.setTextAlign(TextAlign.CENTER);
		sessionContext.fillText(title, 5 + (left + ((columnWidth-10) / 2)), ((bottom - top) / 2) + top, columnWidth-10);
		
		sessionContext.restore();
	}
	
	private void repaintTimeline() {
		timelineContext.clearRect(0,  0, HOUR_BAR_WIDTH, sessionCanvasHeight);
		paintTimeline();
	}
		
	private void paintSessionHourLines() {
		sessionContext.save();		
		int y = 0;
		for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
			y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + HOUR_PERIOD_HEIGHT;
			sessionContext.setStrokeStyle(CssColor.make("rgba(128,128,128,0.5)"));
			sessionContext.beginPath();
			sessionContext.moveTo(0, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			sessionContext.lineTo(sessionCanvasWidth, y);// + (QTR_HOUR_PERIOD_HEIGHT / 4));
			sessionContext.closePath();
			sessionContext.stroke();
		}
		sessionContext.restore();
	}
	
	private int timelineVPos = 0;
	private void paintTimeline() {
		
		// hour markers
		timelineContext.save();
		
		// offset to match vertical scroll of session area
		timelineContext.translate(0, -timelineVPos);
		
		// hour bar background
		timelineContext.setFillStyle(CssColor.make("rgba(128,128,128,0.25)"));
		timelineContext.fillRect(0,  0, HOUR_BAR_WIDTH, sessionCanvasHeight);
		
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
		return layout;
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

	/**
	 * 
	 * @return The number of columns to be rendered
	 */
	private int getTrackColCount() {
		int count = 0;
		if (schedule != null && selectedDate != null) {
			DailySchedule daily = schedule.getDailySchedule(selectedDate);
			Map<SessionTrack, ArrayList<TrackSchedule>> scheds = daily.getTrackSchedules();
			for (Map.Entry<SessionTrack, ArrayList<TrackSchedule>> entry : scheds.entrySet()) {
				count += entry.getValue().size();
			}
		}
		return count;
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
		onResize();
		repaint();
	}
}
