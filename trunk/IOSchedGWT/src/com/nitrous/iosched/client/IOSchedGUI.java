package com.nitrous.iosched.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarController;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledWidget;

public class IOSchedGUI extends Composite implements ActivityController, ToolbarController {
	private static final Images images = GWT.create(Images.class);
	
	private ActivityMenu rootMenu;
	
	// views
	private ScheduleView schedule;
	private MapView map;
	private SessionTrackView sessions;
	private StarredView starred;
	private SandBoxView sandbox;
	private BulletinView bulletin;
	
	private AbsolutePanel layout;
	private DeckPanel viewDeckPanel;
	private Toolbar currentToolbar;
	private ToolbarEnabledWidget currentView;
	
	private static final int WIDTH = 316;
	public IOSchedGUI() {		
		layout = new AbsolutePanel();
		initWidget(layout);		
		layout.setSize(WIDTH+"px", "450px");

		Image colorBar = new Image(images.colors());
		colorBar.setSize(WIDTH+"px", "6px");
		layout.add(colorBar, 0, 43);
		
		int clientHeight = 402;
		viewDeckPanel = new DeckPanel();
		viewDeckPanel.setStyleName("deckPanel");
		viewDeckPanel.setSize(WIDTH+"px", clientHeight+"px");
		layout.add(viewDeckPanel, 0, 49);
		
		// 0
		rootMenu = new ActivityMenu(WIDTH, clientHeight);
		rootMenu.setController(this);
		viewDeckPanel.add(rootMenu);
			
		// 1
		map = new MapView(WIDTH, clientHeight);
		viewDeckPanel.add(map);
		
		// 2
		schedule = new ScheduleView(WIDTH, clientHeight);
		viewDeckPanel.add(schedule);
		
		// 3
		sessions = new SessionTrackView(WIDTH, clientHeight);
		viewDeckPanel.add(sessions);

		// 4
		starred = new StarredView(WIDTH, clientHeight);
		viewDeckPanel.add(starred);
		
		// 5
		sandbox = new SandBoxView(WIDTH, clientHeight);
		viewDeckPanel.add(sandbox);
		
		// 6
		bulletin = new BulletinView(WIDTH, clientHeight);
		viewDeckPanel.add(bulletin);
		
		home();
	}
	
	public void home() {
		viewDeckPanel.showWidget(0);
		currentView = rootMenu;
		showToolbar(currentView);
	}
	
	private void showToolbar(ToolbarEnabledWidget widget) {
		if (currentToolbar != null) {
			layout.remove(currentToolbar.getUI());
		}
		currentToolbar = widget.getToolbar();
		if (currentToolbar != null) {
			currentToolbar.setController(this);
			layout.add(currentToolbar.getUI(), 0, 0);
		}
	}
	
	public void refresh() {
		if (currentView == map) {
			map.reset();
		}
	}
	public void search() {
	}
	
	public void showSchedule() {
		viewDeckPanel.showWidget(2);
		currentView = schedule;
		showToolbar(currentView);
	}
	
	public void showMap() {
		viewDeckPanel.showWidget(1);
		map.reset();
		currentView = map;
		showToolbar(currentView);
	}
	
	public void showSessions() {
		viewDeckPanel.showWidget(3);
		currentView = sessions;
		showToolbar(currentView);
	}
	public void showStarred() {
		viewDeckPanel.showWidget(4);
		currentView = starred;
		showToolbar(currentView);
	}
	public void showSandbox() {
		viewDeckPanel.showWidget(5);
		currentView = sandbox;
		showToolbar(currentView);
	}
	public void showBulletin() {
		viewDeckPanel.showWidget(6);
		currentView = bulletin;
		showToolbar(currentView);
	}

}
