package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.SessionSelectionToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SessionTrackSelectionView extends Composite implements ToolbarEnabledView {
	private static final Images images = GWT.create(Images.class);
	private SessionSelectionToolbar toolbar = new SessionSelectionToolbar();
	private ActivityController controller;
	private IScroll scroll;
	public SessionTrackSelectionView(int width) {
		width -= 20;
		VerticalPanel layout = new VerticalPanel();
		layout.setWidth(width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		initWidget(layout);

		for (SessionTrack track : SessionTrack.values()) {
			HorizontalPanel row = new HorizontalPanel();
			row.setWidth(width+"px");
			row.setStyleName("sessionTrackRow");
			row.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			Label label = new Label(track.toString());
			label.setStyleName("sessionTrackText");
			label.setWidth((width - 40)+"px");
			final SessionTrack clickedTrack = track;
			label.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if (controller != null) {
						controller.showSessionTrack(clickedTrack);
					}
				}
			});
			row.add(label);
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);			
			row.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			Image img = getImage(track);
			if (img != null) {
				img.setStyleName("sessionTrackSwatch");
				img.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						if (controller != null) {
							controller.showSessionTrack(clickedTrack);
						}
					}
				});
				row.add(img);
			} else {
				HorizontalPanel fill = new HorizontalPanel();
				fill.setSize("40px", "40px");
				row.add(fill);
			}
			layout.add(row);
		}
		layout.getElement().setId("SessionTrackSelectionView-scrollpanel");
		scroll = IScroll.applyScroll(layout);
		scroll.refresh();
	}

	private Image getImage(SessionTrack track) {
		switch (track) {
		case All: return null;
		case Android: return new Image(images.trackAndroid());
		case AppEngine: return new Image(images.trackAppEngine());
		case Chrome: return new Image(images.trackChrome());
		case Commerce: return new Image(images.trackCommerce());
		case DevTools: return new Image(images.trackDevTools());
		case Geo: return new Image(images.trackGeo());
		case GoogleAPIs: return new Image(images.trackGoogleAPIs());
		case GoogleApps: return new Image(images.trackGoogleApps());
		case TechTalk: return new Image(images.trackTechTalk());
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
}
