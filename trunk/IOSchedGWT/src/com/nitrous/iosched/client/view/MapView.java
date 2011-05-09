package com.nitrous.iosched.client.view;


public class MapView extends AbstractIFrameView {
	private static final String URL = "http://www.google.com/events/io/2011/embed.html#level1";
	public MapView(int width, int height) {
		super("Map", URL, width, height);
	}
}
