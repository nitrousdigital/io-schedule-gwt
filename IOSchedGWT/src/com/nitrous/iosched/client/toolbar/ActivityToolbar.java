package com.nitrous.iosched.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;

public class ActivityToolbar extends Composite implements Toolbar {
	
	private static Images images = GWT.create(Images.class);
	private Image homeBtn = new Image(images.home());
	private Image refreshBtn = new Image(images.refresh());
	private Image searchBtn = new Image(images.search());

	private ToolbarController toolbarController;
	
	public ActivityToolbar(String label) {
		HorizontalPanel toolbar = new HorizontalPanel();
		initWidget(toolbar);
		
		toolbar.add(homeBtn);
		homeBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.home();
				}
			}
		});
		
		ToolbarText text = new ToolbarText(label);
		toolbar.add(text);
		
		toolbar.add(refreshBtn);
		refreshBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.refresh();
				}
			}
		});
		
		toolbar.add(searchBtn);
		searchBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.search();
				}
			}
		});
		
	}
	
	public ToolbarController getController() {
		return toolbarController;
	}

	public void setController(ToolbarController toolbarController) {
		this.toolbarController = toolbarController;
	}

	public Widget getUI() {
		return this;
	}

}
