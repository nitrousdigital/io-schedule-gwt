package com.nitrous.iosched.client.view;

import java.util.Comparator;
import java.util.TreeSet;

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
import com.nitrous.iosched.client.model.SandboxFeed;
import com.nitrous.iosched.client.model.SandboxFeedEntry;
import com.nitrous.iosched.client.toolbar.SandboxViewToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SandBoxCompanySelectionView extends Composite implements ToolbarEnabledView, Refreshable {
	private SandboxViewToolbar toolbar = new SandboxViewToolbar();
	private CompanyPod companyPod;
	private VerticalPanel layout;
	private int width;
	public SandBoxCompanySelectionView(int width) {
		this.width = width-20;
		layout = new VerticalPanel();
		layout.setWidth(this.width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
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
		RequestBuilder builder = new RequestBuilder(RequestBuilder.GET, "http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json");
		try {
			builder.sendRequest(null, new RequestCallback(){
				public void onResponseReceived(Request request, Response response) {
					if (response.getStatusCode() != Response.SC_OK) {
						showError("Failed to load sandbox data: "+response.getStatusText());
					} else {
						loadSandboxData(response.getText());
					}
				}
				public void onError(Request request, Throwable exception) {
					showError("Failed to load sandbox data: "+exception.getMessage());
				}
			});
		} catch (Exception ex) {
			showError("Failed to load sandbox data: "+ex.getMessage());
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
				if (companyPod == null || CompanyPod.All.equals(companyPod) || companyPod.toString().equalsIgnoreCase(entry.getCompanyPod())) {
					addEntry(entry);
				}
			}
		}
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
		row.add(title);
		layout.add(row);
	}
	
	private void onClear() {
		this.layout.clear();		
	}
	
	private void showError(String error) {
		showMessage("<font color=\"#d72525\">" + error + "</font>", true);
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
	
}
