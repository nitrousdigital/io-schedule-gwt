package com.nitrous.iosched.client.view;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SessionTrackListView extends ResizeComposite implements ToolbarEnabledView {
	private ActivityToolbar toolbar = new ActivityToolbar("Session Tracks");
	private ActivityController controller;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SESSIONS);
	public SessionTrackListView(int width) {
		width -= 20;
		
		SessionTrack[] tracks = SessionTrack.values();
		Grid grid = new Grid(tracks.length, 2);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		grid.getElement().setId("SessionTrackListView");
		grid.setWidth("100%");
		
		for (int row = 0; row < tracks.length; row++) {
			grid.getCellFormatter().setStyleName(row, 0, "sessionTrackCell");
			grid.getCellFormatter().setStyleName(row, 1, "sessionTrackCell");
			grid.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			grid.getCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_MIDDLE);
			grid.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			grid.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
			
			Label label = new Label(tracks[row].toString());
			label.setStyleName("sessionTrackText");
			label.setWidth("100%");
			
			final SessionTrack clickedTrack = tracks[row];
			label.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if (controller != null) {
						controller.showSessionTrack(clickedTrack);
					}
				}
			});
			grid.setWidget(row, 0, label);
			
			Image img = getImage(tracks[row]);
			if (img != null) {
				img.setStyleName("sessionTrackSwatch");
				img.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						if (controller != null) {
							controller.showSessionTrack(clickedTrack);
						}
					}
				});
				grid.setWidget(row, 1, img);
			} else {
				HorizontalPanel fill = new HorizontalPanel();
				fill.setSize("40px", "40px");
				grid.setWidget(row, 1, fill);
			}
		}
		
		ScrollPanel scroll = new ScrollPanel();
		scroll.add(grid);
		initWidget(scroll);
	}

	private Image getImage(SessionTrack track) {
		switch (track) {
		case All: return null;
		case Android: return new Image(Images.INSTANCE.trackAndroid());
		case Chrome: return new Image(Images.INSTANCE.trackChrome());
		case CloudPlatform: return new Image(Images.INSTANCE.trackCloudPlatform());
		case Commerce: return new Image(Images.INSTANCE.trackCommerce());
		case Entrepreneurship: return new Image(Images.INSTANCE.trackEntrepreneurship());
		case GoogleAPIs: return new Image(Images.INSTANCE.trackGoogleAPIs());
		case GoogleDrive: return new Image(Images.INSTANCE.trackGoogleDrive());
		case GoogleMaps: return new Image(Images.INSTANCE.trackGeo());
		case GooglePlus: return new Image(Images.INSTANCE.trackGooglePlus());
		case GoogleTv: return new Image(Images.INSTANCE.trackGoogleTv());
		case TechTalk: return new Image(Images.INSTANCE.trackTechTalk());
		case YouTube: return new Image(Images.INSTANCE.trackYouTube());
		}
		return null;
	}
	public Toolbar getToolbar() {
		return toolbar;
	}

	public ActivityController getController() {
		return controller;
	}

	public void setController(ActivityController controller) {
		this.controller = controller;
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
