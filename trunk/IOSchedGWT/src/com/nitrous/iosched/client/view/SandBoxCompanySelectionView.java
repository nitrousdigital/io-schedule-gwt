package com.nitrous.iosched.client.view;

import java.util.Comparator;
import java.util.TreeSet;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.JsArray;
import com.google.gwt.http.client.Request;
import com.google.gwt.http.client.RequestBuilder;
import com.google.gwt.http.client.RequestCallback;
import com.google.gwt.http.client.Response;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.model.Configuration;
import com.nitrous.iosched.client.model.SandboxFeed;
import com.nitrous.iosched.client.model.SandboxFeedEntry;
import com.nitrous.iosched.client.toolbar.RefreshableSubActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SandBoxCompanySelectionView extends Composite implements ToolbarEnabledView, Refreshable {
	private RefreshableSubActivityToolbar toolbar = new RefreshableSubActivityToolbar("Companies");
	private CompanyPod companyPod;
	private VerticalPanel layout;
	private int width;
	private IScroll scroll;
	private Bookmark bookmark = new Bookmark(BookmarkCategory.SANDBOX);
	public SandBoxCompanySelectionView(int width) {
		this.width = width-20;
		layout = new VerticalPanel();
		layout.setWidth(this.width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		layout.getElement().setId("SandBoxCompanySelectionView-scrollpanel");
		scroll = IScroll.applyScroll(layout);
		initWidget(layout);
	}
	
	/**
	 * Load and display the companies for the specified sandbox pod
	 * @param companyPod The pod to display
	 */
	public void showPodCompanies(CompanyPod companyPod) {
		toolbar.setTitle(companyPod.toString());
		setCompanyPod(companyPod);
		onRefresh();
	}

	public void setCompanyPod(CompanyPod sandbox) {
		this.companyPod = sandbox;
		this.bookmark.setStateToken(sandbox.getHistoryToken());
	}
	
	private void showMessage(String message, boolean isError) {
		onClear();
		Label msg = new Label(message);
		msg.setStyleName(isError ? "sessionErrorMessage" : "sessionMessage");
		layout.add(msg);
	}
	public void onRefresh() {
		showMessage("Loading, Please wait...", false);
		// load all sandbox data in JSON format
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, Configuration.SANDBOX_FEED_URL);
		try {
			builder.sendRequest(null, new RequestCallback(){
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						String detail = null;
						try {
							detail = response.getStatusText();
						} catch (Throwable t) {
							GWT.log("Failed to load sandbox data", t);
							showMessage("Failed to load sandbox data", true);
							return;
						}
						if (detail != null && detail.trim().length() > 0) {
							showMessage("Failed to load sandbox data: "+detail, true);
						} else {
							showMessage("Failed to load sandbox data", true);
						}
						
					} else {
						loadSandboxData(response.getText());
					}
				}
				public void onError(Request request, Throwable exception) {
					GWT.log("Failed to load sandbox data", exception);
					String detail = exception.getMessage();
					if (detail != null && detail.trim().length() > 0) {
						showMessage("Failed to load sandbox data: "+detail, true);
					} else {
						showMessage("Failed to load sandbox data", true);
					}
				}
			});
		} catch (Exception ex) {
			GWT.log("Failed to load sandbox data", ex);
			String detail = ex.getMessage();
			if (detail != null && detail.trim().length() > 0) {
				showMessage("Failed to load sandbox data: "+detail, true);
			} else {
				showMessage("Failed to load sandbox data", true);
			}
		}		
	}
	
	private void loadSandboxData(String json) {
		onClear();
		SandboxFeed feed = SandboxFeed.eval(json);
		JsArray<SandboxFeedEntry> entries = feed.getEntries();
		if (entries != null) {
			// sort
			TreeSet<SandboxFeedEntry> sorted = new TreeSet<SandboxFeedEntry>(feedSorter);			
			for (int i = 0 ; i < entries.length(); i++) {				
				SandboxFeedEntry entry = entries.get(i);
				sorted.add(entry);
			}
			// display
			for (SandboxFeedEntry entry : sorted) {
				if (companyPod == null || CompanyPod.All.equals(companyPod) || companyPod.toString().equalsIgnoreCase(entry.getCompanyPod().trim())) {
					addEntry(entry);
				}
			}
		}
		scroll.refresh();
	}

	private static final FeedEntryComparator feedSorter = new FeedEntryComparator();
	private static class FeedEntryComparator implements Comparator<SandboxFeedEntry> {
		public int compare(SandboxFeedEntry entry, SandboxFeedEntry other) {
			// 1st sort by company name
			int result = entry.getCompanyName().compareToIgnoreCase(other.getCompanyName());
			if (result == 0) {
				// sort by ID
				result = entry.getId().compareTo(other.getId());
			}
			return result;
		}
	}
	
	private void addEntry(SandboxFeedEntry entry) {
		VerticalPanel row = new VerticalPanel();
		row.setWidth(this.width+"px");
		row.setStyleName("sandboxCompanySelectionRow");
		
		Label title = new Label(entry.getCompanyName());
		title.setStyleName("sandboxCompanySelectionTitle");
		title.setWidth("100%");
		row.add(title);
		layout.add(row);
	}
	
	private void onClear() {
		this.layout.clear();
		scroll.refresh();
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
	public String getHistoryToken() {
		return bookmark.toString();
	}
	
}
