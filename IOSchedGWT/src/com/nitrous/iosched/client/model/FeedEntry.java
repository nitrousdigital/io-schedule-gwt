package com.nitrous.iosched.client.model;

import java.util.Date;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.i18n.client.DateTimeFormat;

/**
 * The entries loaded from a session feed using the URL http://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values?alt=json
 * @author Nick
 *
 */
public final class FeedEntry extends JavaScriptObject {
	private static final DateTimeFormat format = DateTimeFormat.getFormat("EEEE MMMM dd hh:mmaa");
	protected FeedEntry() {
	}
	
	public Date getStartDateTime() {
		Date startTime = getStartDateTimeNative();
		if (startTime == null) {
			// Wednesday May 11
			String date = getSessionDate();
			// 4:15pm-5:15pm
			String time = getSessionTime();
			String dateTime = date + " " + time;
			dateTime = dateTime.substring(0, dateTime.indexOf("-"));
			startTime = format.parse(dateTime);
			setStartDateTimeNative(startTime);
		}
		return startTime;
	}

	private native Date getStartDateTimeNative() /*-{
		return this.startDateTime;
	}-*/;
	private native void setStartDateTimeNative(Date startDateTime) /*-{
		this.startDateTime = startDateTime;
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
	 * @return A unique ID for this entry. Example "https://spreadsheets.google.com/feeds/list/tmaLiaNqIWYYtuuhmIyG0uQ/od6/public/values/d180g"
	 */
	public native String getId() /*-{
		return this.id.$t;
	}-*/;
	
	/**
	 * 
	 * @return Example "sessiondate: Tuesday May 10, 
	 * 					sessiontime: 10:15am-11:15am, 
	 * 					sessionroom: 8, 
	 * 					sessiontrack: Geo, 
	 * 					sessionlevel: 101, 
	 * 					sessiontitle: Connecting People and Places, 
	 * 					sessiontags: Maps, Location, Local, Places, Mobile, 
	 * 					sessionspeakers: Marissa Mayer, 
	 * 					sessionabstract: Hear how Google Maps and Google Places connect millions of people to the places around them, and how your applications can do so too., 
	 * 					sessionurl: http://goo.gl/io/9Yplh, 
	 * 					sessionshorturl: http://goo.gl/io/9Yplh, 
	 * 					sessionhashtag: Geo, 
	 * 					sessionslug: connecting-people-and-places, 
	 * 					sessionfeedbackurl: http://www.speakermeter.com/talks/connecting-people-and-places, 
	 * 					sessionfeedbackshorturl: http://goo.gl/SIFrK"
	 */
	public native String getContent() /*-{
		return this.content.$t;
	}-*/;
	
	/**
	 * @return Session date. Example "Wednesday May 11"
	 */
	public native String getSessionDate() /*-{
		return this.gsx$sessiondate.$t;
	}-*/;
	
	/**
	 * @return Session time. Example "4:15pm-5:15pm"
	 */
	public native String getSessionTime() /*-{
		return this.gsx$sessiontime.$t;
	}-*/;
	/**
	 * @return The session room. Example "8"
	 */
	public native String getSessionRoom() /*-{
		return this.gsx$sessionroom.$t;
	}-*/;
	public native String getSessionProduct() /*-{
		return this.gsx$sessionproduct.$t;
	}-*/;
	
	/**
	 * @return The session track. Example "Geo"
	 */
	public native String getSessionTrack() /*-{
		return this.gsx$sessiontrack.$t;
	}-*/;
	
	/**
	 * @return The session level. Example "101"
	 */
	public native String getSessionLevel() /*-{
		return this.gsx$sessionlevel.$t;
	}-*/;
	public native String getSessionTitle() /*-{
		return this.gsx$sessiontitle.$t;
	}-*/;
	
	/**
	 * @return A comma separated list of session tags
	 */
	public native String getSessionTags() /*-{
		return this.gsx$sessiontags.$t;
	}-*/;
	
	/**
	 * @return Comma separated speaker list. Example "Luke Mahé, Jez Fletcher, Justin O'Beirn"
	 */
	public native String getSessionSpeakers() /*-{
		return this.gsx$sessionspeakers.$t;
	}-*/;

	/**
	 * @return Session abstract. Example
	 *         "Maps API applications are accessed on desktop and mobile devices of many shapes and sizes. Each application has unique goals for conveying information effectively and for facilitating user interactions. Learn how to improve user experience by optimizing the presentation of your map and data and by thoughtful user interface design."
	 */
	public native String getSessionAbstract() /*-{
		return this.gsx$sessionabstract.$t;
	}-*/;
	
	/**
	 * @return (Typically empty)
	 */
	public native String getSessionRequirements() /*-{
		return this.gsx$sessionrequirements.$t;
	}-*/;
	
	/**
	 * @return Link to the session info. Example http://www.google.com/events/io/2011/sessions.html#3d-graphics-on-android-lessons-learned-from-google-body
	 */
	public native String getSessionUrl() /*-{
		return this.gsx$sessionurl.$t;
	}-*/;
	/**
	 * @return Link to the session info. Example http://www.google.com/events/io/2011/sessions.html#3d-graphics-on-android-lessons-learned-from-google-body
	 */
	public native String getSessionShortUrl() /*-{
		return this.gsx$sessionshorturl.$t;
	}-*/;
	
	/**
	 * @return Session hash tag, e.g. Android
	 */
	public native String getSessionHashTag() /*-{
		return this.gsx$sessionhashtag.$t;
	}-*/;
	
	/**
	 * @return Example 3d-graphics-android
	 */
	public native String getSessionSlug() /*-{
		return this.gsx$sessionslug.$t;
	}-*/;
	
	/**
	 * @return (Typically empty)
	 */
	public native String getSessionYouTubeUrl() /*-{
		return this.gsx$sessionyoutubeurl.$t;
	}-*/;
	/**
	 * @return (Typically empty)
	 */
	public native String getSessionPdfUrl() /*-{
		return this.gsx$sessionpdfurl.$t;
	}-*/;
	
	/**
	 * @return Long URL to feedback site. Example http://www.speakermeter.com/talks/3d-graphics-android
	 */
	public native String getSessionFeedbackUrl() /*-{
		return this.gsx$sessionfeedbackurl.$t;
	}-*/;
	
	/**
	 * @return Short URL to feedback site. Example http://goo.gl/tUMU4
	 */
	public native String getSessionFeedbackShortUrl() /*-{
		return this.gsx$sessionfeedbackshorturl.$t;
	}-*/;
	
	/**
	 * @return (Typically empty)
	 */
	public native String getSessionNotesUrl() /*-{
		return this.gsx$sessionnotesurl.$t;
	}-*/;
}
