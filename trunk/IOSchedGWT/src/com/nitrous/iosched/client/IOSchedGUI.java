package com.nitrous.iosched.client;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarController;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;
import com.nitrous.iosched.client.view.ActivityController;
import com.nitrous.iosched.client.view.ActivityMenuView;
import com.nitrous.iosched.client.view.BulletinView;
import com.nitrous.iosched.client.view.MapView;
import com.nitrous.iosched.client.view.Refreshable;
import com.nitrous.iosched.client.view.SandBoxSelectionView;
import com.nitrous.iosched.client.view.SandBoxCompanySelectionView;
import com.nitrous.iosched.client.view.ScheduleView;
import com.nitrous.iosched.client.view.SessionTrackSelectionView;
import com.nitrous.iosched.client.view.SessionTrackView;
import com.nitrous.iosched.client.view.StarredView;

public class IOSchedGUI extends Composite implements ActivityController, ToolbarController {
	private static final Images images = GWT.create(Images.class);
	
	private ActivityMenuView rootMenu;
	
	// views
	private ScheduleView schedule;
	private MapView map;
	
	private SessionTrackSelectionView sessionTrackSelectionView;
	private SessionTrackView sessionTrackView;
	
	private StarredView starred;
	private SandBoxSelectionView sandboxSelectionView;
	private SandBoxCompanySelectionView sandBoxCompanySelectionView;
	
	private BulletinView bulletin;
	
	private AbsolutePanel layout;
	private DeckPanel viewDeckPanel;
	private Toolbar currentToolbar;
	private ToolbarEnabledView currentView;
	
	private static final int WIDTH = 316;
	public IOSchedGUI() {		
		layout = new AbsolutePanel();
		initWidget(layout);		
		layout.setSize(WIDTH+"px", "450px");

		Image colorBar = new Image(images.colors());
		colorBar.setSize(WIDTH+"px", "6px");
		layout.add(colorBar, 0, 43);
		
		int clientHeight = 360;
		viewDeckPanel = new DeckPanel();
		viewDeckPanel.setStyleName("deckPanel");
		viewDeckPanel.setSize(WIDTH+"px", clientHeight+"px");
		layout.add(viewDeckPanel, 0, 49);
		
		// 0
		rootMenu = new ActivityMenuView(WIDTH, clientHeight);
		rootMenu.setController(this);
		viewDeckPanel.add(rootMenu);
			
		// 1
		map = new MapView(WIDTH, clientHeight);
		viewDeckPanel.add(map);
		
		// 2
		schedule = new ScheduleView(WIDTH, clientHeight);
		viewDeckPanel.add(schedule);
		
		// 3
		sessionTrackSelectionView = new SessionTrackSelectionView(WIDTH);
		sessionTrackSelectionView.setController(this);
		viewDeckPanel.add(sessionTrackSelectionView);

		// 4
		starred = new StarredView(WIDTH, clientHeight);
		viewDeckPanel.add(starred);
		
		// 5
		sandboxSelectionView = new SandBoxSelectionView(WIDTH, clientHeight);
		sandboxSelectionView.setController(this);
		viewDeckPanel.add(sandboxSelectionView);
		
		// 6
		bulletin = new BulletinView(WIDTH, clientHeight);
		viewDeckPanel.add(bulletin);
		
		// 7 - displays sessions within the selected selection track
		sessionTrackView = new SessionTrackView(WIDTH);
		viewDeckPanel.add(sessionTrackView);
		
		// 8 - displays the list of companies within a selected sandbox pod
		sandBoxCompanySelectionView = new SandBoxCompanySelectionView(WIDTH);
		viewDeckPanel.add(sandBoxCompanySelectionView);
		
		home();
	}
	
	public void back() {
		if (currentView == sessionTrackView) {
			showSessionTrackSelector();
		} else if (currentView == sandBoxCompanySelectionView) {
			showSandboxSelector();
		}
	}
	
	public void home() {
		viewDeckPanel.showWidget(0);
		currentView = rootMenu;
		showToolbar(currentView);
	}
	
	private void showToolbar(ToolbarEnabledView widget) {
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
		if (currentView != null && currentView instanceof Refreshable) {
			((Refreshable)currentView).onRefresh();
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
		map.onRefresh();
		currentView = map;
		showToolbar(currentView);
	}
	
	public void showSessionTrackSelector() {
		viewDeckPanel.showWidget(3);
		currentView = sessionTrackSelectionView;
		showToolbar(currentView);
	}
	public void showStarred() {
		viewDeckPanel.showWidget(4);
		currentView = starred;
		showToolbar(currentView);
	}
	public void showSandboxSelector() {
		viewDeckPanel.showWidget(5);
		currentView = sandboxSelectionView;
		showToolbar(currentView);
	}
	public void showBulletin() {
		viewDeckPanel.showWidget(6);
		currentView = bulletin;
		showToolbar(currentView);
	}
	public void showSessionTrack(SessionTrack track) {
		sessionTrackView.showSessionTrack(track);
		viewDeckPanel.showWidget(7);
		currentView = sessionTrackView;
		showToolbar(currentView);
	}

	public void showCompanyPod(CompanyPod pod) {
		this.sandBoxCompanySelectionView.showPodCompanies(pod);
		viewDeckPanel.showWidget(8);
		currentView = sandBoxCompanySelectionView;
		showToolbar(currentView);
	}

}
