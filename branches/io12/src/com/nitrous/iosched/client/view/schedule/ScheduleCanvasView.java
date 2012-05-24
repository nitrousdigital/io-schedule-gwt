package com.nitrous.iosched.client.view.schedule;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.CanvasGradient;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.canvas.dom.client.TextMetrics;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ScrollEvent;
import com.google.gwt.event.logical.shared.ResizeEvent;
import com.google.gwt.event.logical.shared.ResizeHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DockLayoutPanel;
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
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;
import com.nitrous.iosched.client.view.ActivityController;
import com.nitrous.iosched.client.view.Bookmark;
import com.nitrous.iosched.client.view.BookmarkCategory;
import com.nitrous.iosched.client.view.Refreshable;
import com.nitrous.iosched.client.view.SessionFillStyle;

public class ScheduleCanvasView implements ToolbarEnabledView, IsWidget, Refreshable, ScheduleViewConfig, ScheduleToolbarController {
	
	private ScheduleToolbar toolbar;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SCHEDULE);

	private Canvas sessionCanvas;
	private Context2d sessionContext;
	
	private CategoryHeaderCanvas categoryHeader;
	
	private ActivityController controller;

	private DockLayoutPanel layout;

	private int sessionCanvasWidth;

	private ConferenceSchedule schedule;
	private Date selectedDate;
	
	private int columnWidth;
	
	private ScrollPanel sessionScroll;
	
	private TimelineCanvas timeline;
	
	public ScheduleCanvasView() {
		this.layout = new DockLayoutPanel(Unit.PX);
		
		// sessions
		this.sessionCanvas = Canvas.createIfSupported();
		if (this.sessionCanvas == null) {
			this.layout.add(new Label("Sorry, your browser is not supported"));
			return;
		} 
		this.sessionContext = sessionCanvas.getContext2d();
		this.sessionScroll = new ScrollPanel(sessionCanvas);
		this.sessionScroll.addScrollHandler(new com.google.gwt.event.dom.client.ScrollHandler(){
			@Override
			public void onScroll(ScrollEvent event) {
				onSessionScroll();
			}
		});
		
		// timeline
		this.timeline = new TimelineCanvas();
				
		// category header
		this.categoryHeader = new CategoryHeaderCanvas();

		// main layout
		this.layout.addNorth(categoryHeader, CATEGORY_HEADER_HEIGHT);
		this.layout.addWest(timeline, HOUR_BAR_WIDTH);
		this.layout.add(sessionScroll);
		
		this.toolbar = new ScheduleToolbar();
		this.toolbar.setScheduleController(this);
		
		onResize();
		onRefresh();
		Window.addResizeHandler(new ResizeHandler(){
			@Override
			public void onResize(ResizeEvent event) {
				ScheduleCanvasView.this.onResize();
			}
		});
	}
	
	@Override
	public void navPrevDate() {
		if (schedule == null) {
			return;
		}
		
		// find next date
		Set<Date> dates = this.schedule.getDates();
		if (dates != null && dates.size() > 0) {
			if (this.selectedDate == null) {
				showDate(dates.iterator().next());
			} else {
				Date[] arr = dates.toArray(new Date[dates.size()]);
				if (arr[0] == selectedDate) {
					// first date already selected
					return;
				}
				for (int i = 1; i < arr.length; i++) {
					if (arr[i] == selectedDate) {
						showDate(arr[i-1]);
						return;
					}
				}
			}
		}
	}

	@Override
	public void navNextDate() {
		if (schedule == null) {
			return;
		}
		// find next date
		Set<Date> dates = this.schedule.getDates();
		if (dates != null && dates.size() > 0) {
			if (this.selectedDate == null) {
				showDate(dates.iterator().next());
			} else {
				for (Iterator<Date> i = dates.iterator(); i.hasNext(); ) {
					Date d = i.next();
					if (d == selectedDate) {
						if (i.hasNext()) {
							showDate(i.next());
						}
						return;
					}
				}
			}
		}
	}
	
	private void showDate(Date date) {
		this.selectedDate = date;
		this.sessionScroll.scrollToTop();
		this.sessionScroll.scrollToLeft();
		onResize();
	}
	
	
	private void onSessionScroll() {
		// account for current session scroll vertical position
		int vpos = sessionScroll.getVerticalScrollPosition();
		timeline.onSessionScroll(vpos);
		
		int hpos = sessionScroll.getHorizontalScrollPosition();
		this.categoryHeader.onSessionScroll(hpos);
	}
	
	private void onResize() {
		int winWidth = Window.getClientWidth();
		int winHeight = Window.getClientHeight();
		this.layout.setPixelSize(winWidth, winHeight);
		
		int trackColCount = getTrackColCount();
		
		this.columnWidth = ( winWidth - (HOUR_BAR_WIDTH + COLUMN_SPACE) );
		this.columnWidth = Math.min(columnWidth, MAX_SESSION_COL_WIDTH);
		this.categoryHeader.setColumnWidth(columnWidth);
		this.sessionCanvasWidth = (trackColCount * columnWidth) + (COLUMN_SPACE * (trackColCount > 0 ? trackColCount - 1 : 0));
		
		
		this.sessionCanvas.setPixelSize(sessionCanvasWidth, TIMELINE_CANVAS_HEIGHT);
		this.sessionCanvas.setCoordinateSpaceWidth(sessionCanvasWidth);
		this.sessionCanvas.setCoordinateSpaceHeight(TIMELINE_CANVAS_HEIGHT);
		
		this.categoryHeader.onResize(sessionCanvasWidth);
		
		onSessionScroll();
		repaint();
	}
	
	private void onClear() {
		sessionContext.clearRect(0,  0, sessionCanvasWidth, TIMELINE_CANVAS_HEIGHT);
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
		Map<Integer, SessionTrack> trackColumns = new HashMap<Integer,SessionTrack>();
		for (Map.Entry<SessionTrack, ArrayList<TrackSchedule>> entry : trackScheds.entrySet()) {
			ArrayList<TrackSchedule> sched = entry.getValue();
			for (TrackSchedule s2 : sched) {
				SessionTrack track = s2.getTrack();
				ArrayList<EventDataWrapper> events = s2.getEvents();
				if (events.size() > 0) {
					paintColumnBackground(track, column);
					for (EventDataWrapper event : events) {
						paintEntry(track, event, column);
					}
					trackColumns.put(column, track);
					column++;
				}
			}
		}
		categoryHeader.setTracks(trackColumns);
		
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
	
	private void paintColumnBackground(SessionTrack track, int column) {
		double left = (column * columnWidth) + ( ( column - 1.0D ) * COLUMN_SPACE) + (COLUMN_SPACE / 2.0D);
		double width = columnWidth + COLUMN_SPACE;
		String color = SessionFillStyle.getBackgroundColor(track);
		sessionContext.save();
		sessionContext.setFillStyle(color);
		sessionContext.fillRect(left, 0, width, TIMELINE_CANVAS_HEIGHT);
		sessionContext.restore();
	}
	
	private void paintEntry(SessionTrack track, int startHour, int startMinute, int endHour, int endMinute, String title, int column) {
		int durationMins = ((endHour * 60) + endMinute) - ((startHour * 60) + startMinute);
		int top = ((startHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((startMinute / 15) * QTR_HOUR_PERIOD_HEIGHT);
		int bottom = ((endHour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((endMinute / 15)  * QTR_HOUR_PERIOD_HEIGHT);
		int left = (column * columnWidth) + (column * COLUMN_SPACE);
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
		sessionContext.setTextBaseline(TextBaseline.MIDDLE);
		sessionContext.setFont("12pt Calibri");
		String[] lines = wrap(title);
//		if (lines.length == 1) {
//			sessionContext.setTextAlign(TextAlign.CENTER);
//			sessionContext.fillText(lines[0], 5 + (left + ((columnWidth-10) / 2)), ((bottom - top) / 2) + top, columnWidth-10);
//		} else {
			sessionContext.setTextAlign(TextAlign.LEFT);
			
			double paraWidth = 0D;
			for (String line : lines) {
				TextMetrics metrics = sessionContext.measureText(line);
				paraWidth = Math.max(metrics.getWidth(), paraWidth);
			}
			
			double x = left + (columnWidth / 2) - (paraWidth / 2);
			double y = top + (((bottom - top) / 2) - (((lines.length - 1) * TEXT_LINE_HEIGHT) / 2));
			for (String line : lines) {
				sessionContext.fillText(line, x, y, columnWidth-10);
				y += TEXT_LINE_HEIGHT;
			}
//		}
		
		sessionContext.restore();
	}
	private String[] wrap(String text) {
		text = text.trim();
		int maxLineLen = columnWidth - 10;
		
		TextMetrics metrics = sessionContext.measureText(text);
		if (metrics.getWidth() <= maxLineLen) {
			return new String[]{text};
		}
		
		ArrayList<String> lines = new ArrayList<String>();
		String[] words = text.split(" ");
		StringBuffer line = new StringBuffer();
		for (String word : words) {
			StringBuffer tempLine = new StringBuffer();
			if (line.length() > 0) {
				tempLine.append(line);
				tempLine.append(" ");
			}			
			tempLine.append(word);			
			metrics = sessionContext.measureText(tempLine.toString());
			if (metrics.getWidth() > maxLineLen) {
				// word doesnt fit on current line to add so complete current line and add to next line
				if (line.length() > 0) {
					lines.add(line.toString());
				}
				line = new StringBuffer(word);
			} else {
				// add to current line as it fits
				if (line.length() > 0) {
					line.append(" ");
				}
				line.append(word);
			}			
		}
		if (line.length() > 0) {
			lines.add(line.toString());
		}
		return lines.toArray(new String[lines.size()]);
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
		int left = (column * columnWidth) + (column * COLUMN_SPACE);
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
	}
}
