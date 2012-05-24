package com.nitrous.iosched.client.toolbar;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;

public class ApplicationToolbar extends Composite implements Toolbar {
	
	private static final Image logo = new Image(Images.INSTANCE.logo());
	private static final Image searchBtn = new Image(Images.INSTANCE.search());

	private ToolbarController toolbarController;
	
	public ApplicationToolbar() {
		HorizontalPanel toolbar = new HorizontalPanel();
		toolbar.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		toolbar.setStyleName("toolbarFill");
		initWidget(toolbar);
		
		toolbar.add(logo);
		logo.setStyleName("toolbarButton");
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
//		searchBtn.setStyleName("toolbarButton");
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
