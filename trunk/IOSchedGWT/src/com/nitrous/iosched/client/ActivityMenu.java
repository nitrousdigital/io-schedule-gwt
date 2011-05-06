package com.nitrous.iosched.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.toolbar.RootToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

public class ActivityMenu extends Composite implements ToolbarEnabledWidget {
	private static final Images images = GWT.create(Images.class);
	
	private static final Image bulletinBtn = new Image(images.bulletin());
	private static final Image mapBtn = new Image(images.map());
	private static final Image sandboxBtn = new Image(images.sandbox());
	private static final Image scheduleBtn = new Image(images.schedule());
	private static final Image sessionsBtn = new Image(images.sessions());
	private static final Image starredBtn = new Image(images.starred());
	
	private ActivityController controller;
	private RootToolbar toolbar = new RootToolbar();
	
	/**
	 * @wbp.parser.constructor
	 */
	public ActivityMenu() {
		this(316, 402);
	}
	
	public ActivityMenu(int width, int height) {
		
		AbsolutePanel absolutePanel = new AbsolutePanel();
		absolutePanel.setStyleName("iconBackground");
		initWidget(absolutePanel);
		absolutePanel.setSize(width+"px", height+"px");
				
		absolutePanel.add(scheduleBtn, 58, 14);
		scheduleBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSchedule();
				}
			}
		});
		Label lblSchedule = new Label("Schedule");
		lblSchedule.setStyleName("iconText");
		lblSchedule.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSchedule();
				}
			}
		});
		absolutePanel.add(lblSchedule, 62, 80);
		
		absolutePanel.add(mapBtn, 191, 10);
		mapBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showMap();
				}
			}
		});
		Label lblMap = new Label("Map");
		lblMap.setStyleName("iconText");
		lblMap.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showMap();
				}
			}
		});
		absolutePanel.add(lblMap, 211, 80);
		
		absolutePanel.add(sessionsBtn, 54, 123);
		sessionsBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSessions();
				}
			}
		});
		Label lblSessions = new Label("Sessions");
		lblSessions.setStyleName("iconText");
		lblSessions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSessions();
				}
			}
		});
		absolutePanel.add(lblSessions, 62, 187);
		
		absolutePanel.add(starredBtn, 191, 123);
		starredBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showStarred();
				}
			}
		});
		Label lblStarred = new Label("Starred");
		lblStarred.setStyleName("iconText");
		lblStarred.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showStarred();
				}
			}
		});
		absolutePanel.add(lblStarred, 202, 187);
		
		absolutePanel.add(sandboxBtn, 54, 226);
		sandboxBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSandbox();
				}
			}
		});
		Label lblSandbox = new Label("Sandbox");
		lblSandbox.setStyleName("iconText");
		lblSandbox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSandbox();
				}
			}
		});
		absolutePanel.add(lblSandbox, 58, 294);
		
		absolutePanel.add(bulletinBtn, 192, 226);
		bulletinBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showBulletin();
				}
			}
		});
		Label lblBulletin = new Label("Bulletin");
		lblBulletin.setStyleName("iconText");
		lblBulletin.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showBulletin();
				}
			}
		});
		absolutePanel.add(lblBulletin, 196, 294);
	}

	public ActivityController getController() {
		return controller;
	}

	public void setController(ActivityController controller) {
		this.controller = controller;
	}

	public Toolbar getToolbar() {
		return toolbar;
	}
	
}
