package com.nitrous.iosched.client.view;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.i18n.client.DateTimeFormat;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.SessionCell;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class ScheduleView extends AbstractScrollableComposite implements ToolbarEnabledView {
	private ActivityToolbar toolbar = new ActivityToolbar("Schedule");
	private Label tuesday;
	private Label wednesday;
	private Label navRight;
	private Label navLeft;
	private FlexTable calendarGrid;
	private ScrollPanel layout;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SCHEDULE);
	private ActivityController controller;
	public ScheduleView(int width) {
		width -= 20;
		layout = new ScrollPanel();
		initWidget(layout);

		HorizontalPanel datePanel = new HorizontalPanel();
		datePanel.setSize(width+"px", "12px");
		datePanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		
		navLeft = new Label("<<");
		navLeft.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				navLeft();
			}
		});
		navLeft.setStyleName("scheduleViewDateNavLabel");
		navLeft.setVisible(false);
		datePanel.add(navLeft);
		
		tuesday = new Label("Tue, May 10");
		tuesday.setStyleName("scheduleViewDateLabel");
		datePanel.add(tuesday);
		
		wednesday = new Label("Wed, May 11");
		wednesday.setStyleName("scheduleViewDateLabel");
		wednesday.setVisible(false);
		datePanel.add(wednesday);
		
		navRight = new Label(">>");
		navRight.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				navRight();
			}
		});
		navRight.setStyleName("scheduleViewDateNavLabel");
		datePanel.add(navRight);
		
		
		calendarGrid = new FlexTable();
		calendarGrid.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller == null) {
					return;
				}
				Cell cell = calendarGrid.getCellForEvent(event);
				if (cell != null) {
					int row = cell.getRowIndex();
					int col = cell.getCellIndex();
					onCellClicked(row, col);
				}
			}
		});
		
//		calendarGrid.setBorderWidth(1);
//		calendarGrid.setWidth(width+"px");
		VerticalPanel v = new VerticalPanel();
		v.setSize("100%", "100%");
		v.add(datePanel);
		v.add(calendarGrid);
		layout.add(v);
		
		layout.getElement().setId("ScheduleView-scrollpanel");
		navLeft();
		
		setScrollable(layout);
	}
	
	public void setController(ActivityController controller) {
		this.controller = controller;
	}
	
	private void addHourMarkers() {
		// add hour markers
		int row = 0;
		for (int hour = 7; hour <= 22; hour++) {
			for (int quarter = 0; quarter < 4; quarter++) {
				StringBuilder timeText = new StringBuilder();

				String ampm = "pm";
				int hrText = hour;
				if (hour > 12) {
					hrText = hour -12; 					
				}
				if (hour < 12) {
					ampm = "am";
				}
				if (String.valueOf(hrText).length() == 1) {
					timeText.append("0");
				}
				timeText.append(hrText);
				timeText.append(":");
				switch (quarter) {
					case 0: timeText.append("00"); break;
					case 1: timeText.append("15"); break;
					case 2: timeText.append("30"); break;
					case 3: timeText.append("45"); break;
				}
				timeText.append(ampm);
				
				Label hrLabel = new Label(timeText.toString());
				hrLabel.setStyleName("scheduleViewHourLabel");
				calendarGrid.getFlexCellFormatter().setWidth(row, 0, "40px");
				calendarGrid.setWidget(row++, 0, hrLabel);
			}
		}
	}

	private void onCellClicked(int row, int col) {
		if (col != 2) {
			// currently only support session boxes
			return;
		}
		SessionCell found = null;
		for (SessionCell cell : sessionCells) {
			if (cell.getCol() == col) {
				int cellMinRow = cell.getMinRow();
				int cellMaxRow = cell.getMaxRow();
				if (row >= cellMinRow && row <= cellMaxRow) {
					found = cell;
					break;
				}				
			}
		}
		if (found != null) {
			controller.showSessionTimeRange(found.getStartTime(), found.getEndTime());
		}
	}
	
	private ArrayList<SessionCell> sessionCells = new ArrayList<SessionCell>();
	private void navRight() {
		calendarGrid.removeAllRows();
		sessionCells.clear();
		addHourMarkers();
		
		tuesday.setVisible(false);
		navRight.setVisible(false);
		
		wednesday.setVisible(true);
		navLeft.setVisible(true);
		
		addBlueBox("Breakfast", "Wednesday May 11 2011 7:00am", "Wednesday May 11 2011 10:00am");
		addBlueBox("Lunch<br>served", "Wednesday May 11 2011 11:45am", "Wednesday May 11 2011 1:30pm");
		
		addDisabledRedBox("Keynote", "Wednesday May 11 2011 9:30am", "Wednesday May 11 2011 10:30am");
		addRedBox("Breakout<br>sessions", "Wednesday May 11 2011 10:45am", "Wednesday May 11 2011 11:45am");		
		addRedBox("Breakout<br>sessions", "Wednesday May 11 2011 12:30pm", "Wednesday May 11 2011 1:30pm");		
		addRedBox("Breakout<br>sessions", "Wednesday May 11 2011 1:45pm", "Wednesday May 11 2011 2:45pm");		
		addRedBox("Breakout<br>sessions", "Wednesday May 11 2011 3:00pm", "Wednesday May 11 2011 4:00pm");
		addRedBox("Breakout<br>sessions", "Wednesday May 11 2011 4:15pm", "Wednesday May 11 2011 5:15pm");
		
		addGreenBox("Office<br>hours", "Wednesday May 11 2011 12:00pm", "Wednesday May 11 2011 3:00pm");
		addGreenBox("Office<br>hours", "Wednesday May 11 2011 3:00pm", "Wednesday May 11 2011 5:30pm");
		
		// workaround for bug where FlexTable inserts unwanted empty cells due to parallel rowspans.
		calendarGrid.removeCell(20, 1);
		calendarGrid.removeCell(22, 1);
		
		refreshScroll();
	}
	
	private void navLeft() {
		calendarGrid.removeAllRows();
		addHourMarkers();
		
		tuesday.setVisible(true);
		navRight.setVisible(true);
		
		wednesday.setVisible(false);
		navLeft.setVisible(false);
		
		
		addBlueBox("Breakfast", "Tuesday May 10 2011 7:00am", "Tuesday May 10 2011 10:00am");
		addBlueBox("Lunch<br>served", "Tuesday May 10 2011 11:45am", "Tuesday May 10 2011 1:15pm");
		addBlueBox("After<br>Hours<br>evening<br>party", "Tuesday May 10 2011 6:30pm", "Tuesday May 10 2011 10:15pm");
		
		addDisabledRedBox("Keynote", "Tuesday May 10 2011 9:00am", "Tuesday May 10 2011 10:00am");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 10:15am", "Tuesday May 10 2011 11:15am");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 11:30am", "Tuesday May 10 2011 12:30pm");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 1:15pm", "Tuesday May 10 2011 2:15pm");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 2:30pm", "Tuesday May 10 2011 3:30pm");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 3:45pm", "Tuesday May 10 2011 4:45pm");
		addRedBox("Breakout<br>sessions", "Tuesday May 10 2011 5:00pm", "Tuesday May 10 2011 6:15pm");
		
		addGreenBox("Office<br>hours", "Tuesday May 10 2011 12:00pm", "Tuesday May 10 2011 3:00pm");
		addGreenBox("Office<br>hours", "Tuesday May 10 2011 3:00pm", "Tuesday May 10 2011 6:30pm");
		
		// workaround for bug where FlexTable inserts unwanted empty cells due to parallel rowspans.
		calendarGrid.removeCell(20, 2);
		calendarGrid.removeCell(20, 1);
		calendarGrid.removeCell(32, 2);
		
		refreshScroll();
	}
		
	private void addBlueBox(String text, String startTime, String endTime) {
		addBox(text, startTime, endTime, "blueScheduleBox", "scheduleBoxTextDisabled", 1);
	}
	private void addDisabledRedBox(String text, String startTime, String endTime) {
		addBox(text, startTime, endTime, "redScheduleBoxDisabled", "scheduleBoxTextDisabled", 1);
	}
	private void addRedBox(String text, String startTime, String endTime) {
		addBox(text, startTime, endTime, "redScheduleBox", "scheduleBoxText", 2);
	}
	private void addGreenBox(String text, String startTime, String endTime) {
//		addBox(text, startTime, endTime, "greenScheduleBox", "scheduleBoxText", 3);
		addBox(text, startTime, endTime, "greenScheduleBoxDisabled", "scheduleBoxTextDisabled", 3);
	}
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd yyyy h:mmaa");
	private void addBox(String text, String startTime, String endTime, String boxStyle, String textStyle, int column) {
		Date start = format.parse(startTime);
		Date end = format.parse(endTime);
		int startHr = start.getHours();
		int startMin = start.getMinutes();
		int endHr = end.getHours();
		int endMin = end.getMinutes();
		
		int row = (startHr-7) * 4;
		row += (startMin / 15);
		int endRow = (endHr-7) * 4;
		endRow += (endMin / 15);
		int rowSpan = endRow - row;
		
		VerticalPanel box = new VerticalPanel();
		box.setStyleName(boxStyle);
		box.setSize("100%", "100%");
		
		HTML boxLabel = new HTML(text);		
		boxLabel.setStyleName(textStyle);
		boxLabel.setSize("100%", "100%");
		
		box.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		box.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		box.add(boxLabel);
		calendarGrid.setWidget(row, column, box);
		if (rowSpan > 1) {
			calendarGrid.getFlexCellFormatter().setRowSpan(row, column, rowSpan);
		}
		calendarGrid.getFlexCellFormatter().setStyleName(row, column, boxStyle);
		calendarGrid.getFlexCellFormatter().setWidth(row, column, "70px");
		
		sessionCells.add(new SessionCell(row, row + rowSpan, column, start.getTime(), end.getTime()));
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
