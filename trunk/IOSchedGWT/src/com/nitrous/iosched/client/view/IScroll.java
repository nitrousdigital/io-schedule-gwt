package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.ui.Widget;

public final class IScroll extends JavaScriptObject {
	protected IScroll() {
	}
	
	/**
	 * Must be called if the content of the scrollable div is updated.
	 */
	public native void refresh() /*-{
		setTimeout(function () {
			this.refresh();
		}, 3000);
	}-*/;
	
	/**
	 * Initialize a scrolling region for the specified widget
	 * @param widget The widget whose content is to be scrollable on the iPhone
	 * @return The reference to the JavaScript iScroll API created for the specified widget
	 */
	public static IScroll applyScroll(Widget widget) {
		return applyScroll(widget.getElement());
	}
	
	
	/**
	 * Initialize a scrolling region for the specified element.
	 * @param element The element whose content is to be scrollable on the iPhone
	 * @return The reference to the JavaScript iScroll API created for the specified element
	 */
	private static native IScroll applyScroll(com.google.gwt.user.client.Element element) /*-{
		var scroll = new $wnd.iScroll(element, {vScrollbar: true});
		return scroll;
	}-*/;
}