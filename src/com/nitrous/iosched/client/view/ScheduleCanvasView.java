package com.nitrous.iosched.client.view;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class ScheduleCanvasView implements ToolbarEnabledView, IsWidget {
	private ActivityToolbar toolbar = new ActivityToolbar("Schedule");
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SCHEDULE);

	private Canvas canvas;
	private Context2d context;
	private ActivityController controller;

	private Widget widget;

	private int width;
	private int height;
	public ScheduleCanvasView(int width) {
		canvas = Canvas.createIfSupported();
		if (canvas == null) {
			this.widget = new Label("Sorry, your browser is not supported");
			return;
		} 
		
		this.widget = new ScrollPanel(canvas);
		this.width = 1000;
		this.height = (HOUR_PERIOD_HEIGHT * ((MAX_HOUR - MIN_HOUR) + 1)) + 10;
		
		this.canvas.setPixelSize(width, height);
		this.canvas.setCoordinateSpaceWidth(width);
		this.canvas.setCoordinateSpaceHeight(height);
		this.context = canvas.getContext2d();
		repaint();
	}
	
	private static final int QTR_HOUR_PERIOD_HEIGHT = 20;
	private static final int HOUR_PERIOD_HEIGHT = QTR_HOUR_PERIOD_HEIGHT * 4;
	private static final int MIN_HOUR = 7;
	private static final int MAX_HOUR = 22;
	private void repaint() {
		context.clearRect(0,  0, width, height);
		// hour markers
		context.save();
		context.setFillStyle("black");
		context.setFont("12pt Calibri");
		
		int y = 0;
		for (int hour = MIN_HOUR; hour <= MAX_HOUR; hour++) {
			int minute = 0;
//			for (int minute = 0; minute < 60; minute+= 15) {
				y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + ((minute / 15) + 1) * QTR_HOUR_PERIOD_HEIGHT;
				String timeStr = toTimeStr(hour, minute);
				context.fillText(timeStr, 5, y);
				y = ((hour - MIN_HOUR) * HOUR_PERIOD_HEIGHT) + HOUR_PERIOD_HEIGHT;
//			}
			context.setStrokeStyle(CssColor.make("rgba(128,128,128,0.5)"));
			context.beginPath();
			context.moveTo(0, y + (QTR_HOUR_PERIOD_HEIGHT/4));
			context.lineTo(width, y + (QTR_HOUR_PERIOD_HEIGHT / 4));
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
