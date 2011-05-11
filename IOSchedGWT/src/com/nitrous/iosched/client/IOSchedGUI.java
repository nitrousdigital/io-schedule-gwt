package com.nitrous.iosched.client;

import java.util.ArrayList;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.AbsolutePanel;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.DeckPanel;
import com.google.gwt.user.client.ui.Image;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.model.FeedEntry;
import com.nitrous.iosched.client.model.SessionStore;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarController;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;
import com.nitrous.iosched.client.view.ActivityController;
import com.nitrous.iosched.client.view.ActivityMenuView;
import com.nitrous.iosched.client.view.Bookmark;
import com.nitrous.iosched.client.view.BulletinView;
import com.nitrous.iosched.client.view.MapView;
import com.nitrous.iosched.client.view.NowPlayingView;
import com.nitrous.iosched.client.view.Refreshable;
import com.nitrous.iosched.client.view.SandBoxCompanySelectionView;
import com.nitrous.iosched.client.view.SandBoxSelectionView;
import com.nitrous.iosched.client.view.ScheduleView;
import com.nitrous.iosched.client.view.ScrollableView;
import com.nitrous.iosched.client.view.SessionDetailView;
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
	private SessionDetailView sessionDetailView;
	
	private NowPlayingView nowPlayingView;
	
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
//		viewDeckPanel.setSize(WIDTH+"px", clientHeight+"px");
		viewDeckPanel.setSize(WIDTH+"px", "100%");
		layout.add(viewDeckPanel, 0, 49);
		
		// 0
		rootMenu = new ActivityMenuView(WIDTH, clientHeight);
		rootMenu.setController(this);
		viewDeckPanel.add(rootMenu);
			
		// 1
		map = new MapView(WIDTH, clientHeight);
		viewDeckPanel.add(map);
		
		// 2
		schedule = new ScheduleView(WIDTH);
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
		sessionTrackView.setController(this);
		viewDeckPanel.add(sessionTrackView);
		
		// 8 - displays the list of companies within a selected sandbox pod
		sandBoxCompanySelectionView = new SandBoxCompanySelectionView(WIDTH);
		viewDeckPanel.add(sandBoxCompanySelectionView);
		
		// 9 - displays the session details
		sessionDetailView = new SessionDetailView(WIDTH);
		viewDeckPanel.add(sessionDetailView);
		
		// 10 - now playing session list
		nowPlayingView = new NowPlayingView(WIDTH);
		nowPlayingView.setController(this);
		viewDeckPanel.add(nowPlayingView);
		
	}
	
	public void navigateTo(Bookmark bookmark) {
		if (bookmark == null) {
			return;
		}
		switch (bookmark.getCategory()) {
		case BULLETIN:
			showBulletin();
			break;
		case HOME:
			home();
			break;
		case MAP:
			showMap();
			break;
		case SANDBOX:
			if (bookmark.hasState()) {
				CompanyPod pod = CompanyPod.parseHistoryToken(bookmark.getState().get(0));
				showCompanyPod(pod);
			} else {
				showSandboxSelector();
			}
			break;
			
		case SCHEDULE:
			showSchedule();
			break;
			
		case NOW_PLAYING:
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() == 1) {
					SessionStore.get().getSession(state.get(0), new AsyncCallback<FeedEntry>(){
						public void onFailure(Throwable caught) {
							GWT.log("Failed to load session info for ID: "+state.get(1));
							showNowPlaying();
						}

						public void onSuccess(FeedEntry result) {
							showCurrentSessionDetail(result);
						}						
					});
				}
			} else {
				showNowPlaying();
			}
			break;
			
		case SESSION:
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() == 1) {
					SessionTrack track = SessionTrack.parseHistoryToken(state.get(0));
					showSessionTrack(track);
				} else if (state.size() == 2) {
					final SessionTrack track = SessionTrack.parseHistoryToken(state.get(0));
					SessionStore.get().getSession(state.get(1), new AsyncCallback<FeedEntry>(){
						public void onFailure(Throwable caught) {
							GWT.log("Failed to load session info for ID: "+state.get(1));
							showSessionTrack(track);
						}

						public void onSuccess(FeedEntry result) {
							sessionTrackView.setTrack(track);
							showSessionDetail(track, result);
						}						
					});
				}
			} else {
				showSessionTrackSelector();
			}
			break;
		case STARRED:
			showStarred();
		}
	}
	
	public void back() {
		if (currentView == sessionTrackView) {
			showSessionTrackSelector();
		} else if (currentView == sandBoxCompanySelectionView) {
			showSandboxSelector();
		} else if (currentView == sessionDetailView) {
			SessionTrack track = sessionTrackView.getTrack();
			if (track != null) {
				showSessionTrack(track);
			} else {
				showNowPlaying();
			}
		}
	}
	
	public void home() {
		viewDeckPanel.showWidget(0);
		onViewDisplayed(rootMenu);
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
		onViewDisplayed(schedule);
	}
	
	private void onViewDisplayed(ToolbarEnabledView view) {
		currentView = view;
		showToolbar(currentView);
		if (currentView != null) {
			if (currentView instanceof ScrollableView) {
				((ScrollableView)currentView).initScroll();
			}
			setHistoryToken(currentView.getHistoryToken());
		}
	}
	
	public void showMap() {
		viewDeckPanel.showWidget(1);
		map.onRefresh();
		onViewDisplayed(map);
	}
	
	public void showNowPlaying() {
		viewDeckPanel.showWidget(10);
		nowPlayingView.onRefresh(true);
		onViewDisplayed(nowPlayingView);
	}
	
	public void showSessionTrackSelector() {
		viewDeckPanel.showWidget(3);
		onViewDisplayed(sessionTrackSelectionView);
	}
	public void showSessionTrack(SessionTrack track) {
		sessionTrackView.showSessionTrack(track);
		viewDeckPanel.showWidget(7);
		onViewDisplayed(sessionTrackView);
	}
	public void showSessionDetail(SessionTrack track, FeedEntry entry) {
		sessionDetailView.showSessionDetail(track, entry);
		viewDeckPanel.showWidget(9);
		onViewDisplayed(sessionDetailView);
	}
	public void showCurrentSessionDetail(FeedEntry entry) {
		sessionDetailView.showSessionDetail(entry);
		viewDeckPanel.showWidget(9);
		onViewDisplayed(sessionDetailView);
	}
	
	public void showStarred() {
		viewDeckPanel.showWidget(4);
		onViewDisplayed(starred);
	}
	public void showSandboxSelector() {
		viewDeckPanel.showWidget(5);
		onViewDisplayed(sandboxSelectionView);
	}
	public void showBulletin() {
		viewDeckPanel.showWidget(6);
		onViewDisplayed(bulletin);
	}
	private void setHistoryToken(String token) {
		String old = History.getToken();
		if (old != null && !old.equals(token)) {
			History.newItem(token, false);
		}
	}

	public void showCompanyPod(CompanyPod pod) {
		this.sandBoxCompanySelectionView.showPodCompanies(pod);
		viewDeckPanel.showWidget(8);
		onViewDisplayed(sandBoxCompanySelectionView);
	}


}
