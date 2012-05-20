package com.nitrous.iosched.client.view;

import com.nitrous.iosched.client.model.SessionTrack;

public class SessionFillStyle {
	public static String getTextColor(SessionTrack track) {
		switch (track) {
		case GooglePlus:
			return "white";
		default:
			return "black";
		}
	}
	public static String getStartGradientColor(SessionTrack track) {
		switch (track) {
		case Android:
			return "8caa31";
		case Chrome:
			return "007ab0";
		case CloudPlatform:
			return "b59239";
		case Commerce:
			return "8c0021";
		case Entrepreneurship:
			return "ad7910";
		case GoogleAPIs:
			return "004563";
		case GoogleDrive:
			return "006d29";
		case GoogleMaps:
			return "005939";
		case GooglePlus:
			return "2c005e";
		case GoogleTv:
			return "8c0066";
		case TechTalk:
			return "6b6d6b";
		case YouTube:
			return "b52021";
		default:
			return "rgba(128,128,128,1)";
		}
	}
	public static String getEndGradientColor(SessionTrack track) {
		switch (track) {
		case Android:
			return "a5c739";
		case Chrome:
			return "08aaff";
		case CloudPlatform:
			return "ffcf52";
		case Commerce:
			return "ce0031";
		case Entrepreneurship:
			return "f7ae18";
		case GoogleAPIs:
			return "00658c";
		case GoogleDrive:
			return "009a39";
		case GoogleMaps:
			return "007d5a";
		case GooglePlus:
			return "3f0074";
		case GoogleTv:
			return "ce0096";
		case TechTalk:
			return "9c9a9c";
		case YouTube:
			return "ff3031";
		default:
			return "rgba(255,255,255,1)";
			
		}
	}
}
