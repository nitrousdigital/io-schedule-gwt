package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SandBoxListView extends AbstractScrollableComposite implements ToolbarEnabledView {
	private static final Images images = GWT.create(Images.class);
	private ActivityToolbar toolbar = new ActivityToolbar("Sandbox pods");
	private ActivityController controller;
	private Grid grid;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SANDBOX);
	public SandBoxListView(int width, int height) {
		width -= 20;
		
		CompanyPod[] pods = CompanyPod.values();
		grid = new Grid(pods.length, 2);
		grid.setCellPadding(0);
		grid.setCellSpacing(0);
		grid.setWidth("100%");
		grid.getElement().setId("SandBoxListView");
		
		for (int row = 0; row < pods.length; row++) {
			grid.getCellFormatter().setStyleName(row, 0, "sandboxSelectionCell");
			grid.getCellFormatter().setStyleName(row, 1, "sandboxSelectionCell");
			grid.getCellFormatter().setVerticalAlignment(row, 0, HasVerticalAlignment.ALIGN_MIDDLE);
			grid.getCellFormatter().setVerticalAlignment(row, 1, HasVerticalAlignment.ALIGN_MIDDLE);
			grid.getCellFormatter().setHorizontalAlignment(row, 0, HasHorizontalAlignment.ALIGN_LEFT);
			grid.getCellFormatter().setHorizontalAlignment(row, 1, HasHorizontalAlignment.ALIGN_LEFT);
			
			Label label = new Label(pods[row].toString());
			label.setStyleName("sandboxSelectionRowText");
			label.setWidth("100%");
			
			final CompanyPod clicked = pods[row];
			label.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if (controller != null) {
						controller.showCompanyPod(clicked);
					}
				}
			});
			grid.setWidget(row, 0, label);
			
			Image img = getImage(clicked);
			if (img != null) {
				img.setStyleName("sandboxSelectionSwatch");
				img.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						if (controller != null) {
							controller.showCompanyPod(clicked);
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
		setScrollable(scroll);
	}
	
	private Image getImage(CompanyPod pod) {
		switch (pod) {
			case All: return null;
			case Android: return new Image(images.trackAndroid());
			case AppEngine: return new Image(images.trackAppEngine());
			case Chrome: return new Image(images.trackChrome());
			case Commerce: return new Image(images.trackCommerce());
			case DevTools: return new Image(images.trackDevTools());
			case Geo: return new Image(images.trackGeo());
			case GoogleApps: return new Image(images.trackGoogleApps());
			case Accessibility: return new Image(images.sandboxAccessibility()); 
			case GameDev:return new Image(images.sandboxGameDev());
			case GoogleTv:return new Image(images.sandboxGoogleTv());
			case YouTube:return new Image(images.sandboxYouTube());
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
