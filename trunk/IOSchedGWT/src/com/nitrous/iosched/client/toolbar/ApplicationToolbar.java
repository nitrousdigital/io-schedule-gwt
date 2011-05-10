package com.nitrous.iosched.client.toolbar;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;

public class ApplicationToolbar extends Composite implements Toolbar {
	
	private static final Images images = GWT.create(Images.class);
	private static final Image logo = new Image(images.logo());
	private static final Image searchBtn = new Image(images.search());

	private ToolbarController toolbarController;
	
	public ApplicationToolbar() {
		HorizontalPanel toolbar = new HorizontalPanel();
		initWidget(toolbar);
		
		toolbar.add(logo);
		logo.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.home();
				}
			}
		});
		
		ToolbarText fill = new ToolbarText(" ", 45);
		toolbar.add(fill);
		
		toolbar.add(new ToolbarText(" ", 45));
//		toolbar.add(searchBtn);
//		searchBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
//			public void onClick(ClickEvent event) {
//				if (toolbarController != null) {
//					toolbarController.search();
//				}
//			}
//		});
		
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
