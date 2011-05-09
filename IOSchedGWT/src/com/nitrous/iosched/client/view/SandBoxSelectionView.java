package com.nitrous.iosched.client.view;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Image;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.nitrous.iosched.client.images.Images;
import com.nitrous.iosched.client.model.CompanyPod;
import com.nitrous.iosched.client.toolbar.ActivityToolbar;
import com.nitrous.iosched.client.toolbar.Toolbar;
import com.nitrous.iosched.client.toolbar.ToolbarEnabledView;

public class SandBoxSelectionView extends Composite implements ToolbarEnabledView {
	private static final Images images = GWT.create(Images.class);
	private ActivityToolbar toolbar = new ActivityToolbar("Sandbox pods");
	private ActivityController controller;
	private IScroll scroll;
	public SandBoxSelectionView(int width, int height) {
		width -= 20;
		
		VerticalPanel layout = new VerticalPanel();
		layout.setWidth(width+"px");
		layout.setVerticalAlignment(HasVerticalAlignment.ALIGN_TOP);
		layout.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
		initWidget(layout);

		for (CompanyPod pod: CompanyPod.values()) {
			HorizontalPanel row = new HorizontalPanel();
			row.setWidth(width+"px");
			row.setStyleName("sandboxSelectionRow");
			row.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_LEFT);
			Label label = new Label(pod.toString());
			label.setStyleName("sandboxSelectionRowText");
			label.setWidth((width - 40)+"px");
			final CompanyPod clicked = pod;
			label.addClickHandler(new ClickHandler(){
				public void onClick(ClickEvent event) {
					if (controller != null) {
						controller.showCompanyPod(clicked);
					}
				}
			});
			row.add(label);
			row.setHorizontalAlignment(HasHorizontalAlignment.ALIGN_RIGHT);			
			row.setVerticalAlignment(HasVerticalAlignment.ALIGN_MIDDLE);
			Image img = getImage(clicked);
			if (img != null) {
				img.setStyleName("sandboxSelectionSwatch");
				img.addClickHandler(new ClickHandler(){
					public void onClick(ClickEvent event) {
						if (controller != null) {
							controller.showCompanyPod(clicked);
						}
					}
				});
				row.add(img);
			} else {
				HorizontalPanel fill = new HorizontalPanel();
				fill.setSize("40px", "40px");
				row.add(fill);
			}
			layout.add(row);
		}
		scroll = IScroll.applyScroll(layout);
		scroll.refresh();
	}

	private Image getImage(CompanyPod pod) {
		switch (pod) {
			case All: return null;
			case Android: return new Image(images.trackAndroid());
			case AppEngine: return new Image(images.trackAppEngine());
			case Chrome: return new Image(images.trackChrome());
			case Commerce: return new Image(images.trackCommerce());
			case DevTools: return new Image(images.trackDevTools());
			case Geo: return new Image(images.trackGeo());
			case GoogleApps: return new Image(images.trackGoogleApps());
			case Accessibility: return new Image(images.sandboxAccessibility()); 
			case GameDev:return new Image(images.sandboxGameDev());
			case GoogleTv:return new Image(images.sandboxGoogleTv());
			case YouTube:return new Image(images.sandboxYouTube());
		}
		return null;
	}
	
	public Toolbar getToolbar() {
		return toolbar;
	}
	
	public ActivityController getController() {
		return controller;
	}

	public void setController(ActivityController controller) {
		this.controller = controller;
	}
}
