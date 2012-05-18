package com.nitrous.iosched.client.images;

import com.google.gwt.resources.client.ClientBundle;
import com.google.gwt.resources.client.ImageResource;

public interface Images extends ClientBundle {
	// toolbar icons
	@Source("logo.png")
	ImageResource logo();
	@Source("home.png")
	ImageResource home();
	@Source("refresh.png")
	ImageResource refresh();
	@Source("search.png")
	ImageResource search();
	@Source("back.png")
	ImageResource back();
	
	// color bar
	@Source("colors.png")
	ImageResource colors();

	// activity icons
	@Source("map.png")
	ImageResource map();
	@Source("sandbox.png")
	ImageResource sandbox();
	@Source("bulletin.png")
	ImageResource bulletin();
	@Source("schedule.png")
	ImageResource schedule();
	@Source("sessions.png")
	ImageResource sessions();
	@Source("starred.png")
	ImageResource starred();
	
	// track swatches
	@Source("google_apps_track.png")
	ImageResource trackGoogleApps();
	@Source("dev_tools_track.png")
	ImageResource trackDevTools();
	@Source("app_engine_track.png")
	ImageResource trackAppEngine();
	@Source("android_track.png")
	ImageResource trackAndroid();
	@Source("cloud_platform_track.png")
	ImageResource trackCloudPlatform();
	@Source("chrome_track.png")
	ImageResource trackChrome();
	@Source("commerce_track.png")
	ImageResource trackCommerce();
	@Source("google_drive_track.png")
	ImageResource trackGoogleDrive();
	@Source("geo_track.png")
	ImageResource trackGeo();
	@Source("google_api_track.png")
	ImageResource trackGoogleAPIs();
	@Source("entrepreneurship_track.png")
	ImageResource trackEntrepreneurship();
	@Source("tech_talk_track.png")
	ImageResource trackTechTalk();
	@Source("google_tv_track.png")
	ImageResource trackGoogleTv();
	
	// sandbox swatches
	@Source("accessibility_sandbox.png")
	ImageResource sandboxAccessibility(); 	
	@Source("gamedev_sandbox.png")
	ImageResource sandboxGameDev();
	@Source("googletv_sandbox.png")
	ImageResource sandboxGoogleTv();
	@Source("youtube_sandbox.png")
	ImageResource sandboxYouTube();
	
	
}
