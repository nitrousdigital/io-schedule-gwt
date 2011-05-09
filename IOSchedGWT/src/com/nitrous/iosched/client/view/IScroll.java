package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.JavaScriptObject;

public final class IScroll extends JavaScriptObject {
	protected IScroll() {
	}
	
	/**
	 * Must be called if the content of the scrollable div is updated.
	 */
	public native void refresh() /*-{
		setTimeout(function () {
			this.refresh();
		}, 0);
	}-*/;
	
	/**
	 * Initialize a scrolling region for the specified element.
	 * @param element The element whose content is to be scrollable on the iPhone
	 * @return The reference to the JavaScript iScroll API created for the specified element
	 */
	public static native IScroll applyScroll(com.google.gwt.user.client.Element element) /*-{
		var scroll = new $wnd.iScroll(element, {vScrollbar: true});
		return scroll;
	}-*/;
}
