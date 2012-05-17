package com.nitrous.iosched.client.io2011.model;

import com.google.gwt.core.client.JavaScriptObject;

/**
 * The entries loaded from a session feed using the URL http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values?alt=json
 * @author Nick
 *
 */
public final class SandboxFeedEntry extends JavaScriptObject {
	protected SandboxFeedEntry() {
	}
	
	/**
	 * @return A unique ID for this entry. Example "https://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od4/public/values/ciyn3"
	 */
	public native String getId() /*-{
		return this.id.$t;
	}-*/;
	
	
	/**
	 * @return Example "2011-05-04T00:15:01.165Z"
	 */
	public native String getUpdated() /*-{
		return this.updated.$t;
	}-*/;
	
	/**
	 * @return Example "Row: 64"
	 */
	public native String getTitle() /*-{
		return this.title.$t;
	}-*/;
	
	/**
	 * 
	 * @return Example "companyname: Apps4Android, Inc., 
	 * 	companydesc: World’s largest developer of Android applications designed to support people with disabilities, individuals 65+ years of age, and consumers who never learned to read, 
	 * 	companyurl: http://apps4android.org, 
	 *  companyproductdesc: Accessibly-designed Android applications including, TeleDroid Personal Healthcare Manager®, IDEAL Access 4 Carriers, IDEAL Web Access Pack, IDEAL Item Identifier, IDEAL Magnifier, and Accessible ePub Reader®, 
	 *  companypod: Accessibility "
	 */
	public native String getContent() /*-{
		return this.content.$t;
	}-*/;
	
	public native String getCompanyName() /*-{
		return this.gsx$companyname.$t;
	}-*/;
	public native String getCompanyLocation() /*-{
		return this.gsx$companylocation.$t;
	}-*/;
	public native String getCompanyDescription() /*-{
		return this.gsx$companydesc.$t;
	}-*/;
	public native String getCompanyUrl() /*-{
		return this.gsx$companyurl.$t;
	}-*/;
	/**
	 * @return The company product description. Example "Benetech's open source Android accessible e-book reader enables people with print disabilities to access over 100K books via text to speech technology."
	 */
	public native String getCompanyProductDescription() /*-{
		return this.gsx$companyproductdesc.$t;
	}-*/;
	public native String getCompanyLogoImageUrl() /*-{
		return this.gsx$companylogo.$t;
	}-*/;
	/**
	 * The sandbox pod e.g. Chrome, Accessibility, Android, App Engine, Commerce, Developer Tools, Geo, Google Apps, Google TV or YouTube
	 * @return The sandbox pod e.g. Chrome, Accessibility, Android, App Engine, Commerce, Developer Tools, Geo, Google Apps, Google TV or YouTube
	 */
	public native String getCompanyPod() /*-{
		return this.gsx$companypod.$t;
	}-*/;
	
	/**
	 * Typically empty
	 * @return Typically empty
	 */
	public native String getCompanyTags() /*-{
		return this.gsx$companytags.$t;
	}-*/;
	/**
	 * Typically empty
	 * @return Typically empty
	 */
	public native String getCompanyAddDate() /*-{
		return this.gsx$companyadddate.$t;
	}-*/;
	
	
}
