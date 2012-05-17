package com.nitrous.iosched.client.data;

import com.google.gwt.core.client.JavaScriptObject;

public final class SpeakerData extends JavaScriptObject {
	protected SpeakerData() {
	}
	
	public native String getBio() /*-{
		return this.bio;
	}-*/;
	
	public native String getFirstName() /*-{
		return this.first_name;
	}-*/;
	public native String getLastName() /*-{
		return this.last_name;
	}-*/;
	public native String getDisplayName() /*-{
		return this.display_name;
	}-*/;
	
	public native String getPlusOneUrl() /*-{
		return this.plusone_url;
	}-*/;
	
	public native String getThumbnailUrl() /*-{
		return this.thumbnail_url;
	}-*/;
	
	public native String getCurrentLocation() /*-{
		return this.current_location;
	}-*/;
	public native String getAboutHtml() /*-{
		return this.about;
	}-*/;
	public native String getEmail() /*-{
		return this.email;
	}-*/;
}
