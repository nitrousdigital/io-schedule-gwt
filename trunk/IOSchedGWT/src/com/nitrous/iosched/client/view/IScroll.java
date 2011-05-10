package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.event.logical.shared.AttachEvent;
import com.google.gwt.user.client.ui.Widget;

public final class IScroll extends JavaScriptObject {
	protected IScroll() {
	}
	
	public void refresh() {
		 Scheduler.get().scheduleDeferred(new ScheduledCommand(){
			public void execute() {
				com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
					public void run() {
						_refresh();
					}
				};
				t.schedule(100);
			}
		 });
	}
	
	/**
	 * Must be called if the content of the scrollable div is updated.
	 */
	private native void _refresh() /*-{
		this.refresh();
	}-*/;
	
	/**
	 * Initialize a scrolling region for the specified widget
	 * @param widget The widget whose content is to be scrollable on the iPhone
	 * @return The reference to the JavaScript iScroll API created for the specified widget
	 */
	public static IScroll applyScroll(Widget widget) {
		final IScroll scroll = applyScroll(widget.getElement());
		widget.addAttachHandler(new AttachEvent.Handler(){
			public void onAttachOrDetach(AttachEvent event) {
				if (event.isAttached()) {
					scroll.refresh();
				}
			}
		});
		return scroll;
	}
	
	/**
	 * Initialize a scrolling region for the specified element.
	 * @param element The element whose content is to be scrollable on the iPhone
	 * @return The reference to the JavaScript iScroll API created for the specified element
	 */
	private static native IScroll applyScroll(com.google.gwt.user.client.Element element) /*-{
		var scroll = new $wnd.iScroll(element);
		return scroll;
	}-*/;
}
