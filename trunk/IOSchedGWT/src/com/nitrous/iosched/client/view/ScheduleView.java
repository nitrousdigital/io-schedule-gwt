package com.nitrous.iosched.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class ScheduleView extends Composite implements ToolbarEnabledView {
	private ActivityToolbar toolbar = new ActivityToolbar("Schedule");
	private Label tuesday;
	private Label wednesday;
	private Label navRight;
	private Label navLeft;
	private FlexTable calendarGrid;
	private IScroll scroll;
	public ScheduleView(int width, int height) {
		width -= 20;
		VerticalPanel layout = new VerticalPanel();
		initWidget(layout);

		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		
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
		
		layout.add(datePanel);
		
		calendarGrid = new FlexTable();
//		calendarGrid.setBorderWidth(1);
//		calendarGrid.setWidth(width+"px");
		layout.add(calendarGrid);
		
		layout.getElement().setId("ScheduleView-scrollpanel");
		scroll = IScroll.applyScroll(layout);
		navLeft();
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

	private void navRight() {
		calendarGrid.removeAllRows();
		addHourMarkers();
		
		tuesday.setVisible(false);
		navRight.setVisible(false);
		
		wednesday.setVisible(true);
		navLeft.setVisible(true);
		
		addBlueBox("Breakfast", 7,0, 10,0);
		addBlueBox("Lunch<br>served", 11,45, 13,30);
		
		addDisabledRedBox("Keynote", 9,30, 10,30);
		addRedBox("Breakout<br>sessions", 10,45, 11,45);		
		addRedBox("Breakout<br>sessions", 12,30, 13,30);		
		addRedBox("Breakout<br>sessions", 13,45, 14,45);		
		addRedBox("Breakout<br>sessions", 15,00, 16,0);
		addRedBox("Breakout<br>sessions", 16,15, 17,15);
		
		addGreenBox("Office<br>hours", 12,0, 15,0);
		addGreenBox("Office<br>hours", 15,0, 17,30);
		
		// workaround for bug where FlexTable inserts unwanted empty cells due to parallel rowspans.
		calendarGrid.removeCell(20, 1);
		calendarGrid.removeCell(22, 1);
		
		scroll.refresh();
	}
	
	private void navLeft() {
		calendarGrid.removeAllRows();
		addHourMarkers();
		
		tuesday.setVisible(true);
		navRight.setVisible(true);
		
		wednesday.setVisible(false);
		navLeft.setVisible(false);
		
		
		addBlueBox("Breakfast", 7,0, 10,0);
		addBlueBox("Lunch<br>served", 11,45, 13,15);
		addBlueBox("After<br>Hours<br>evening<br>party", 18,30, 22,15);
		
		addDisabledRedBox("Keynote", 9,0, 10,0);
		addRedBox("Breakout<br>sessions", 10,15, 11,15);
		addRedBox("Breakout<br>sessions", 11,30, 12,30);
		addRedBox("Breakout<br>sessions", 13,15, 14,15);
		addRedBox("Breakout<br>sessions", 14,30, 15,30);
		addRedBox("Breakout<br>sessions", 15,45, 16,45);
		addRedBox("Breakout<br>sessions", 17,0, 18,15);
		
		addGreenBox("Office<br>hours", 12,0, 15,0);
		addGreenBox("Office<br>hours", 15,0, 18,30);
		
		// workaround for bug where FlexTable inserts unwanted empty cells due to parallel rowspans.
		calendarGrid.removeCell(20, 2);
		calendarGrid.removeCell(20, 1);
		calendarGrid.removeCell(32, 2);
		
		scroll.refresh();
	}
	
	private void addBlueBox(String text, int startHr, int startMin, int endHr, int endMin) {
		addBox(text, startHr, startMin, endHr, endMin, "blueScheduleBox", 1);
	}
	private void addDisabledRedBox(String text, int startHr, int startMin, int endHr, int endMin) {
		addBox(text, startHr, startMin, endHr, endMin, "redScheduleBoxDisabled", 1);
	}
	private void addRedBox(String text, int startHr, int startMin, int endHr, int endMin) {
		addBox(text, startHr, startMin, endHr, endMin, "redScheduleBox", 2);
	}
	private void addGreenBox(String text, int startHr, int startMin, int endHr, int endMin) {
		addBox(text, startHr, startMin, endHr, endMin, "greenScheduleBox", 3);
	}
	private void addBox(String text, int startHr, int startMin, int endHr, int endMin, String boxStyle, int column) {
		int row = (startHr-7) * 4;
		row += (startMin / 15);
		int endRow = (endHr-7) * 4;
		endRow += (endMin / 15);
		int rowSpan = endRow - row;
		
		VerticalPanel box = new VerticalPanel();
		box.setStyleName(boxStyle);
		box.setSize("100%", "100%");
		
		HTML boxLabel = new HTML(text);		
		boxLabel.setStyleName("scheduleBoxText");
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
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
}
