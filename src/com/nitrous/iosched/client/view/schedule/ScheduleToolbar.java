package com.nitrous.iosched.client.view.schedule;

import java.util.Date;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;

public class ScheduleToolbar extends ActivityToolbar {
	private ScheduleToolbarController controller;
	public ScheduleToolbar() {
		super("Schedule");
	}
	
	public void setScheduleController(ScheduleToolbarController toolbarController) {
		this.controller = toolbarController;
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
				initNavBackButton(),
				initDateLabel(),
				initNavForwardButton()
		};
	}

	public void setDate(Date date) {
		DateToolbarLabel label = getDateLabel();
		label.setDate(date);
	}
	
	public void setCanNavigateBackward(boolean enabled) {
		Image img = (Image)getWidgets()[2];
		img.setResource(enabled ? Images.INSTANCE.back() : Images.INSTANCE.backDisabled());
	}
	
	public void setCanNavigateForward(boolean enabled) {
		Image img = (Image)getWidgets()[4];
		img.setResource(enabled ? Images.INSTANCE.forward() : Images.INSTANCE.forwardDisabled());
	}	
	
	private DateToolbarLabel getDateLabel() {
		return (DateToolbarLabel)getWidgets()[3];
	}
		
	protected Widget initDateLabel() {
		DateToolbarLabel text = new DateToolbarLabel();
		return text;
	}
	
	protected Widget initNavBackButton() {
		Image backBtn = new Image(Images.INSTANCE.back());
		backBtn.setStyleName("toolbarButton");
		backBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.navPrevDate();
				}
			}
		});
		return  backBtn;
	}
	
	protected Widget initNavForwardButton() {
		Image forwardBtn = new Image(Images.INSTANCE.forward());
		forwardBtn.setStyleName("toolbarButton");
		forwardBtn.addClickHandler(new com.google.gwt.event.dom.client.ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.navNextDate();
				}
			}
		});
		return  forwardBtn;
	}
	
}
