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
	private ToolbarController toolbarController;
	protected ToolbarText toolbarTitle;
	
	public ActivityToolbar(String label) {
		HorizontalPanel toolbar = new HorizontalPanel();
		initWidget(toolbar);
		
		Widget[] widgets = initWidgets(label);
		for (Widget w : widgets) {
			toolbar.add(w);
		}
	}
	
	/**
	 * Build the widgets to add to the toolbar
	 * @param toolbarLabel The label for the toolbar
	 * @return The widgets to add to the toolbar
	 */
	protected Widget[] initWidgets(String toolbarLabel) {
		return new Widget[]{
				initHomeButton(),
				initToolbarLabel(toolbarLabel),
				initRefreshButton(),
				initSearchButton()
		};
	}
	
	protected Widget initHomeButton() {
		Image homeBtn = new Image(images.home());
		homeBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.home();
				}
			}
		});
		return  homeBtn;
	}

	protected Widget initBackButton() {
		Image backBtn = new Image(images.back());
		backBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.back();
				}
			}
		});
		return  backBtn;
	}
	
	/**
	 * Modify the title of the toolbar (if one has been added to the toolbar).
	 */
	public void setTitle(String title) {
		if (toolbarTitle != null) {
			toolbarTitle.setText(title);
		}
	}
	protected ToolbarText initToolbarLabel(String label) {
		toolbarTitle = new ToolbarText(label);
		return toolbarTitle;
	}

	protected Widget initRefreshButton() {
		Image refreshBtn = new Image(images.refresh());
		refreshBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.refresh();
				}
			}
		});
		return refreshBtn;
	}
	
	protected Widget initSearchButton() {
		Image searchBtn = new Image(images.search());
		searchBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (toolbarController != null) {
					toolbarController.search();
				}
			}
		});
		return searchBtn;
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
