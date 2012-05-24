package com.nitrous.iosched.client.view;

import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DockLayoutPanel;
import com.google.gwt.user.client.ui.Grid;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.toolbar.ApplicationToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

/**
 * The main activity menu
 * @author Nick
 *
 */
public class HomeMenuView extends ResizeComposite implements ToolbarEnabledView {
	
	private static final Image bulletinBtn = new Image(Images.INSTANCE.bulletin());
	private static final Image mapBtn = new Image(Images.INSTANCE.map());
	private static final Image sandboxBtn = new Image(Images.INSTANCE.sandbox());
	private static final Image scheduleBtn = new Image(Images.INSTANCE.schedule());
	private static final Image sessionsBtn = new Image(Images.INSTANCE.sessions());
//	private static final Image starredBtn = new Image(images.starred());
	
	private VerticalPanel scheduleCell;
	private VerticalPanel mapCell;
	private VerticalPanel sessionCell;
	private VerticalPanel bulletinCell;
	private VerticalPanel sandboxCell;
//	private VerticalPanel starredCell;
	
	
	private Grid iconGrid;
	private DockLayoutPanel dockLayoutPanel;
	private ActivityController controller;
	private ApplicationToolbar toolbar = new ApplicationToolbar();
	
	private Bookmark bookmark = new Bookmark(BookmarkCategory.HOME);
	private static final int CELL_HEIGHT = 74;
	private static final int LINK_ROW_HEIGHT = 35;
	
	/**
	 * @wbp.parser.constructor
	 */
	public HomeMenuView() {
//		this(316, 402);
		this(Window.getClientWidth(), Window.getClientHeight());
	}
	
	public HomeMenuView(int width, int height) {
		

		// schedule
		scheduleCell = new VerticalPanel();
		scheduleCell.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
		scheduleCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		scheduleCell.setSize("100%", CELL_HEIGHT+"px");
		
		scheduleCell.add(scheduleBtn);
		scheduleBtn.setStyleName("iconImage");
		scheduleBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSchedule();
				}
			}
		});
		
		Label lblSchedule = new Label("Schedule");
		lblSchedule.setStyleName("iconText");
		scheduleCell.add(lblSchedule);
		lblSchedule.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSchedule();
				}
			}
		});

		// map
		mapCell = new VerticalPanel();
		mapCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		mapCell.setSize("100%", CELL_HEIGHT+"px");
		
		mapCell.add(mapBtn);
		mapBtn.setStyleName("iconImage");
		mapBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showMap();
				}
			}
		});
		
		Label lblMap = new Label("Map");
		lblMap.setStyleName("iconText");
		mapCell.add(lblMap);
		lblMap.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showMap();
				}
			}
		});
		
		// sessions
		sessionCell = new VerticalPanel();
		sessionCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		sessionCell.setSize("100%", CELL_HEIGHT+"px");
		
		sessionCell.add(sessionsBtn);
		sessionsBtn.setStyleName("iconImage");
		sessionsBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSessionTrackSelector();
				}
			}
		});		
		
		Label lblSessions = new Label("Sessions");
		lblSessions.setStyleName("iconText");
		sessionCell.add(lblSessions);
		lblSessions.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSessionTrackSelector();
				}
			}
		});
		
		// bulletin
		bulletinCell = new VerticalPanel();
		bulletinCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		bulletinCell.setSize("100%", CELL_HEIGHT+"px");
		bulletinCell.add(bulletinBtn);
		bulletinBtn.setStyleName("iconImage");
		bulletinBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showBulletin();
				}
			}
		});
		
		Label lblBulletin = new Label("Bulletin");
		lblBulletin.setStyleName("iconText");
		bulletinCell.add(lblBulletin);
		lblBulletin.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showBulletin();
				}
			}
		});
		
		// sandbox
		sandboxCell = new VerticalPanel();
		sandboxCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		sandboxCell.setSize("100%", CELL_HEIGHT+"px");
		sandboxCell.add(sandboxBtn);
		sandboxBtn.setStyleName("iconImage");
		sandboxBtn.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSandboxSelector();
				}
			}
		});
		
		Label lblSandbox = new Label("Sandbox");
		lblSandbox.setStyleName("iconText");
		sandboxCell.add(lblSandbox);
		lblSandbox.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showSandboxSelector();
				}
			}
		});
		
		// starred
//		starredCell = new VerticalPanel();
//		starredCell.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
//		starredCell.setSize("100%", CELL_HEIGHT+"px");
//		
//		starredCell.add(starredBtn);
//		starredBtn.setStyleName("iconImage");
//		starredBtn.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				if (controller != null) {
//					controller.showStarred();
//				}
//			}
//		});
//		
//		Label lblStarred = new Label("Starred");
//		starredCell.add(lblStarred);
//		lblStarred.setStyleName("iconText");
//		lblStarred.addClickHandler(new ClickHandler(){
//			public void onClick(ClickEvent event) {
//				if (controller != null) {
//					controller.showStarred();
//				}
//			}
//		});
		
		// links
	
		// now playing
		Button btnNowPlaying = new Button("Now Playing");
		btnNowPlaying.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showNowPlaying();
				}
			}
		});

		// realtime stream
		Button btnRealtime = new Button("Realtime Stream");
		btnRealtime.addClickHandler(new ClickHandler(){
			public void onClick(ClickEvent event) {
				if (controller != null) {
					controller.showRealtimeStream();
				}
			}
		});
		
		HorizontalPanel linkPanel = new HorizontalPanel();
		linkPanel.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_CENTER);
		linkPanel.setVerticalAlignment(HasVerticalAlignment.ALIGN_BOTTOM);
		linkPanel.setSize("100%", LINK_ROW_HEIGHT+"px");
		linkPanel.add(btnNowPlaying);
		linkPanel.add(btnRealtime);
		
		dockLayoutPanel = new DockLayoutPanel(Unit.EM);
		dockLayoutPanel.setSize(width+"px", height+"px");
		dockLayoutPanel.setStyleName("iconBackground");
		dockLayoutPanel.addSouth(linkPanel, 4.1);
		initWidget(dockLayoutPanel);
		
		updateIconLayout(width, height);
	}

	private int iconRowCount = 0;
	private void updateIconLayout(int width, int height) {
		int rowCount = 0;
		int colCount = 0;
		if (height >= (LINK_ROW_HEIGHT + (3 * CELL_HEIGHT))) {
			// portrait
			rowCount = 3;
			colCount = 2;
		} else if (height >= (LINK_ROW_HEIGHT + (2 * CELL_HEIGHT))) {
			// landscape
			rowCount = 2;
			colCount = 3;
		} else {
			// thin landscape
			rowCount = 1;
			colCount = 6;
		}
		
		if (rowCount != this.iconRowCount) {
			if (iconGrid != null) {
				dockLayoutPanel.remove(iconGrid);
			}
			iconGrid = new Grid(rowCount, colCount);
			iconGrid.setSize("100%", "100%");
			Widget[] icons = {
					scheduleCell, mapCell,
					sessionCell, bulletinCell,
					sandboxCell //,starredCell
			};
			int row = 0;
			int col = -1;
			for (int i = 0; i < icons.length; i++) {
				col++;
				if (col == colCount) {
					col = 0;
					row++;
				}
				iconGrid.setWidget(row, col, icons[i]);
			}			
			dockLayoutPanel.add(iconGrid);
			this.iconRowCount = rowCount;
		}
	}

	public void onResize(int width, int height) {
		dockLayoutPanel.setSize(width+"px", height+"px");
		updateIconLayout(width, height);
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
	
	public String getHistoryToken() {
		return bookmark.toString();
	}
}
