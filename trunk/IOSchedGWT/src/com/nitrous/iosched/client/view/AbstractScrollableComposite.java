package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.Scheduler;
import com.google.gwt.core.client.Scheduler.ScheduledCommand;
import com.google.gwt.user.client.ui.ResizeComposite;
import com.google.gwt.user.client.ui.Widget;

public abstract class AbstractScrollableComposite extends ResizeComposite implements ScrollableView {
	private IPhoneScroller scroll;
	private Widget scrollable;
	public AbstractScrollableComposite() {
	}
	protected void setScrollable(Widget w) {
		this.scrollable = w;
	}
	public void initScroll() {
		if (scroll == null && scrollable != null) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand(){
				public void execute() {
					com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
						public void run() {
							scroll = new IPhoneScroller(scrollable);
						}
					};
					t.schedule(300);
				}
			 });
		}
	}
	public void refreshScroll() {
		if (this.scroll != null) {
			Scheduler.get().scheduleDeferred(new ScheduledCommand(){
				public void execute() {
					com.google.gwt.user.client.Timer t = new com.google.gwt.user.client.Timer() {
						public void run() {
							scroll.refresh();
						}
					};
					t.schedule(300);
				}
			 });
		}
	}
}
