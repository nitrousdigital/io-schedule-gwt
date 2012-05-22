package com.nitrous.iosched.client.view.schedule;

import java.util.Map;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.Context2d.TextAlign;
import com.google.gwt.canvas.dom.client.Context2d.TextBaseline;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.view.SessionFillStyle;

public class CategoryHeaderCanvas implements IsWidget, ScheduleViewConfig {
	private AbsolutePanel container;
	private Canvas headerCanvas;
	private Canvas cornerFillCanvas;
	private Context2d context;
	
	private int width;
	
	private Map<Integer, SessionTrack> trackColumns;
	private int columnWidth;
	private int hpos;
	
	public CategoryHeaderCanvas() {
		this.cornerFillCanvas = Canvas.createIfSupported();
		this.cornerFillCanvas.setPixelSize(HOUR_BAR_WIDTH, CATEGORY_HEADER_HEIGHT);
		this.cornerFillCanvas.setCoordinateSpaceWidth(HOUR_BAR_WIDTH);
		this.cornerFillCanvas.setCoordinateSpaceHeight(CATEGORY_HEADER_HEIGHT);
		Context2d ctx = cornerFillCanvas.getContext2d();
		ctx.save();
		ctx.setFillStyle(CssColor.make(CORNER_BACKGROUND_COLOR));
		ctx.fillRect(0,  0, HOUR_BAR_WIDTH, CATEGORY_HEADER_HEIGHT);
		
		this.container = new AbsolutePanel();
		this.container.add(cornerFillCanvas, 0, 0);
		this.cornerFillCanvas.getElement().getStyle().setZIndex(100);
		
		this.headerCanvas = Canvas.createIfSupported();
		this.context = headerCanvas.getContext2d();
		this.container.add(headerCanvas, 0, 0);
		this.headerCanvas.getElement().getStyle().setZIndex(90);
		this.headerCanvas.getElement().getStyle().setLeft(HOUR_BAR_WIDTH - hpos, Unit.PX);
	}
	
	public void onSessionScroll(int hpos) {
		if (hpos != this.hpos) {
			this.hpos = hpos;
			this.headerCanvas.getElement().getStyle().setLeft(HOUR_BAR_WIDTH - hpos, Unit.PX);
		}
	}
	
	public void setColumnWidth(int columnWidth) {
		this.columnWidth = columnWidth;
		repaint();
	}
	
	public void onResize(int sessionCanvasWidth) {
		this.width = sessionCanvasWidth;
		this.headerCanvas.setPixelSize(sessionCanvasWidth, CATEGORY_HEADER_HEIGHT);
		this.headerCanvas.setCoordinateSpaceWidth(sessionCanvasWidth);
		this.headerCanvas.setCoordinateSpaceHeight(CATEGORY_HEADER_HEIGHT);
	}
	
	public void setTracks(Map<Integer, SessionTrack> trackColumns) {
		this.trackColumns = trackColumns;
		repaint();
	}
	
	public void repaint() {
		onClear();
		if (trackColumns == null) {
			return;
		}
		
		for (int column = 0 ; ; column++) {
			SessionTrack track = trackColumns.get(column);
			if (track == null) {
				break;
			}
			double left = (column * columnWidth) + ( ( column - 1.0D ) * COLUMN_SPACE) + (COLUMN_SPACE / 2.0D);
			double width = columnWidth + COLUMN_SPACE;
			String color = SessionFillStyle.getHeaderBackgroundColor(track);
			context.save();
			context.setFillStyle(color);
			context.fillRect(left, 0, width, CATEGORY_HEADER_HEIGHT);
			
			// content label
			context.setFillStyle(SessionFillStyle.getTextColor(track));
			context.setFont("14pt Calibri");
			context.setTextAlign(TextAlign.CENTER);
			context.setTextBaseline(TextBaseline.MIDDLE);
			context.fillText(track.toString(), 5 + (left + ((columnWidth-10) / 2)), (CATEGORY_HEADER_HEIGHT / 2), columnWidth-10);
			context.restore();
		}
	}
	
	private void onClear() {
		this.context.clearRect(0, 0, width, CATEGORY_HEADER_HEIGHT);
	}
	
	@Override
	public Widget asWidget() {
		return container;
	}

}
