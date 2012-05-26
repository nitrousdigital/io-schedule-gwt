package com.nitrous.iosched.client;

import java.util.ArrayList;
import java.util.Date;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.History;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.DeckLayoutPanel;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.io2011.model.SessionFeedEntry;
import com.nitrous.iosched.client.io2011.model.SessionStore;
import com.nitrous.iosched.client.io2011.view.ScheduleView;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.model.SessionTrack;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarController;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;
import com.nitrous.iosched.client.view.ActivityController;
import com.nitrous.iosched.client.view.Bookmark;
import com.nitrous.iosched.client.view.BulletinView;
import com.nitrous.iosched.client.view.CompanyListView;
import com.nitrous.iosched.client.view.HomeMenuView;
import com.nitrous.iosched.client.view.MapView;
import com.nitrous.iosched.client.view.NowPlayingView;
import com.nitrous.iosched.client.view.RealtimeView;
import com.nitrous.iosched.client.view.Refreshable;
import com.nitrous.iosched.client.view.SandBoxListView;
import com.nitrous.iosched.client.view.ScrollableView;
import com.nitrous.iosched.client.view.SessionDetailView;
import com.nitrous.iosched.client.view.SessionListView;
import com.nitrous.iosched.client.view.SessionTrackListView;
import com.nitrous.iosched.client.view.StarredView;
import com.nitrous.iosched.client.view.schedule.ScheduleCanvasView;

public class IOSchedGUI extends ResizeComposite implements ActivityController, ToolbarController {
	
	private HomeMenuView rootMenu;
	
	// views
	private ScheduleCanvasView scheduleView;
	private MapView mapView;
	private RealtimeView realtimeStreamView;
	
	private SessionTrackListView sessionTrackListView;
	private SessionListView sessionListView;
	private SessionDetailView sessionDetailView;
	
	private NowPlayingView nowPlayingView;
	
	private StarredView starredView;
	private SandBoxListView sandboxListView;
	private CompanyListView companyListView;
	
	private BulletinView bulletinView;
	
	private DockLayoutPanel layout;
	private DeckLayoutPanel viewDeckPanel;
	private Toolbar currentToolbar;
	private ToolbarEnabledView currentView;
	
	private static final int WIDTH = Window.getClientWidth();
	private static final int HEIGHT = Window.getClientHeight();

	private HorizontalPanel toolbarContainer;
	
	public IOSchedGUI() {		

		toolbarContainer = new HorizontalPanel();
		toolbarContainer.getElement().setId("toolbar-container");
		toolbarContainer.setHeight("45px");
		toolbarContainer.setWidth("100%");
		
//		Image colorBar = new Image(images.colors());
//		colorBar.setSize(WIDTH+"px", "6px");
		HorizontalPanel colorBar = new HorizontalPanel();
		colorBar.setWidth("100%");
		colorBar.setStyleName("colorBar");
		
		DockLayoutPanel header = new DockLayoutPanel(Unit.PX);
		header.setStyleName("toolbarFill");
		header.setWidth("100%");
		header.getElement().setId("header");
		header.addSouth(colorBar, 6);
		header.add(toolbarContainer);
//		header.setHeight("51px");
		
		viewDeckPanel = new DeckLayoutPanel();
		viewDeckPanel.setStyleName("deckPanel");
		viewDeckPanel.setSize("100%", "100%");
		
		// 0
		rootMenu = new HomeMenuView(WIDTH, HEIGHT-51);
		rootMenu.setController(this);
		viewDeckPanel.add(rootMenu);
			
		// 1
		mapView = new MapView(WIDTH, HEIGHT-51);
		viewDeckPanel.add(mapView);
		
		// 2
		scheduleView = new ScheduleCanvasView();
		scheduleView.setController(this);
		viewDeckPanel.add(scheduleView);
		
		// 3
		sessionTrackListView = new SessionTrackListView(WIDTH);
		sessionTrackListView.setController(this);
		viewDeckPanel.add(sessionTrackListView);

		// 4
		starredView = new StarredView(WIDTH, HEIGHT-51);
		viewDeckPanel.add(starredView);
		
		// 5
		sandboxListView = new SandBoxListView(WIDTH, HEIGHT-51);
		sandboxListView.setController(this);
		viewDeckPanel.add(sandboxListView);
		
		// 6
		bulletinView = new BulletinView(WIDTH, HEIGHT-51);
		viewDeckPanel.add(bulletinView);
		
		// 7 - displays sessions within the selected selection track
		sessionListView = new SessionListView(WIDTH);
		sessionListView.setController(this);
		viewDeckPanel.add(sessionListView);
		
		// 8 - displays the list of companies within a selected sandbox pod
		companyListView = new CompanyListView(WIDTH);
		viewDeckPanel.add(companyListView);
		
		// 9 - displays the session details
		sessionDetailView = new SessionDetailView(WIDTH);
		viewDeckPanel.add(sessionDetailView);
		
		// 10 - now playing session list
		nowPlayingView = new NowPlayingView(WIDTH);
		nowPlayingView.setController(this);
		viewDeckPanel.add(nowPlayingView);
		
		// 11 - realtime stream
		realtimeStreamView = new RealtimeView(WIDTH, HEIGHT-51);
		viewDeckPanel.add(realtimeStreamView);	

		layout = new DockLayoutPanel(Unit.PX);
		//layout.setSize(WIDTH+"px", "450px");
		layout.setSize(WIDTH+"px", HEIGHT+"px");
		layout.addNorth(header, 50);
		layout.add(viewDeckPanel);
		
		initWidget(layout);		
	}
	
	public void onResize() {
		super.onResize();
		onResize(Window.getClientWidth(), Window.getClientHeight());
	}
	private void onResize(int width, int height) {
		layout.setSize(width+"px", height+"px");
		rootMenu.onResize(width, height-51);
		mapView.onResize(width, height-51);
		bulletinView.onResize(width, height-51);
		realtimeStreamView.onResize(width, height-51);
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
		case REALTIME:
			showRealtimeStream();
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
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() > 0) {
					try {
						Long l = Long.parseLong(state.get(0));
						scheduleView.showDate(new Date(l.longValue()));
					} catch (Exception ex) {
					}
				}						
			}
			showSchedule();
			break;
			
		case NOW_PLAYING:
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() == 1) {
					SessionStore.get().getSession(state.get(0), new AsyncCallback<SessionFeedEntry>(){
						public void onFailure(Throwable caught) {
							GWT.log("Failed to load session info for ID: "+state.get(1));
							showNowPlaying();
						}

						public void onSuccess(SessionFeedEntry result) {
							showCurrentSessionDetail(result);
						}						
					});
				}
			} else {
				showNowPlaying();
			}
			break;
		
		case SCHEDULE_SESSIONS:
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() == 2) {
					try {
						long startTime = Long.valueOf(state.get(0));
						long endTime = Long.valueOf(state.get(1));
						showSessionTimeRange(startTime, endTime);
					} catch (Throwable t) {
						GWT.log("Failed to parse start/end date range for session view", t);
						showSchedule();
					}
				} else {
					showSchedule();
				}
			} else {
				showSchedule();
			}
			break;
			
		case SESSIONS:
			if (bookmark.hasState()) {
				final ArrayList<String> state = bookmark.getState();
				if (state.size() == 1) {
					SessionTrack track = SessionTrack.parseHistoryToken(state.get(0));
					showSessionTrack(track);
				} else if (state.size() == 2) {
					final SessionTrack track = SessionTrack.parseHistoryToken(state.get(0));
					SessionStore.get().getSession(state.get(1), new AsyncCallback<SessionFeedEntry>(){
						public void onFailure(Throwable caught) {
							GWT.log("Failed to load session info for ID: "+state.get(1));
							showSessionTrack(track);
						}

						public void onSuccess(SessionFeedEntry result) {
							sessionListView.setTrack(track);
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
		if (currentView == sessionListView) {
			if (sessionListView.getTrack() != null) {
				showSessionTrackSelector();
			} else {
				showSchedule();
			}
		} else if (currentView == companyListView) {
			showSandboxSelector();
		} else if (currentView == sessionDetailView) {
			SessionTrack track = sessionDetailView.getReferringTrack();
			if (track != null) {
				// referring view is track view
				showSessionTrack(track);
			} else {
				Long start = sessionDetailView.getReferringSessionBlockStartTime();
				if (start != null) {
					// referring view is time block selected from schedule view
					Long end = sessionDetailView.getReferringSessionBlockEndTime();
					showSessionTimeRange(start, end);
				} else {
					// referring view is now playing view
					showNowPlaying();
				}
			}
		}
	}
	
	public void home() {
		viewDeckPanel.showWidget(0);
		onViewDisplayed(rootMenu);
	}
	
	private void showToolbar(ToolbarEnabledView widget) {
		if (currentToolbar != null) {
			toolbarContainer.remove(currentToolbar.getUI());
		}
		currentToolbar = widget.getToolbar();
		if (currentToolbar != null) {
			currentToolbar.setController(this);
			Widget toolbar = currentToolbar.getUI();
			toolbar.setWidth("100%");
			toolbarContainer.add(toolbar);
		}
	}
	
	public void refresh() {
		if (currentView != null && currentView instanceof Refreshable) {
			((Refreshable)currentView).onRefresh();
		}
	}
	public void search() {
	}
	
	public void showRealtimeStream() {
		viewDeckPanel.showWidget(11);
		realtimeStreamView.onRefresh();
		onViewDisplayed(realtimeStreamView);
	}
	
	public void showSchedule() {
		viewDeckPanel.showWidget(2);
		onViewDisplayed(scheduleView);
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
		mapView.onRefresh();
		onViewDisplayed(mapView);
	}
	
	public void showNowPlaying() {
		viewDeckPanel.showWidget(10);
		nowPlayingView.onRefresh(true);
		onViewDisplayed(nowPlayingView);
	}
	
	public void showSessionTrackSelector() {
		viewDeckPanel.showWidget(3);
		onViewDisplayed(sessionTrackListView);
	}
	public void showSessionTrack(SessionTrack track) {
		sessionListView.showSessionTrack(track);
		viewDeckPanel.showWidget(7);
		onViewDisplayed(sessionListView);
	}
	/**
	 * Display all sessions within the specified time range
	 * @param startTime The start time
	 * @param endTime The end time
	 */
	public void showSessionTimeRange(long startTime, long endTime) {
		sessionListView.showSessionTimeRange(startTime, endTime);
		viewDeckPanel.showWidget(7);
		onViewDisplayed(sessionListView);
	}

	public void showSessionDetail(SessionTrack track, SessionFeedEntry entry) {
		sessionDetailView.showSessionDetail(track, entry);
		viewDeckPanel.showWidget(9);
		onViewDisplayed(sessionDetailView);
	}
	/**
	 * Show the details of a session selected from a time range view
	 * @param entry The session to show
	 * @param blockStartTime The start of the time range from which the session was selected
	 * @param blockEndTime The end of the time range from which the session was selected
	 */
	public void showSessionDetail(SessionFeedEntry entry, long blockStartTime, long blockEndTime) {
		sessionDetailView.showSessionDetail(entry, blockStartTime, blockEndTime);
		viewDeckPanel.showWidget(9);
		onViewDisplayed(sessionDetailView);
	}
	
	public void showCurrentSessionDetail(SessionFeedEntry entry) {
		sessionDetailView.showSessionDetail(entry);
		viewDeckPanel.showWidget(9);
		onViewDisplayed(sessionDetailView);
	}
	
	public void showStarred() {
		viewDeckPanel.showWidget(4);
		onViewDisplayed(starredView);
	}
	public void showSandboxSelector() {
		viewDeckPanel.showWidget(5);
		onViewDisplayed(sandboxListView);
	}
	public void showBulletin() {
		viewDeckPanel.showWidget(6);
		onViewDisplayed(bulletinView);
	}
	public static void setHistoryToken(String token) {
		String old = History.getToken();
		if (old != null && !old.equals(token)) {
			History.newItem(token, false);
		}
	}

	public void showCompanyPod(CompanyPod pod) {
		this.companyListView.showPodCompanies(pod);
		viewDeckPanel.showWidget(8);
		onViewDisplayed(companyListView);
	}


}
