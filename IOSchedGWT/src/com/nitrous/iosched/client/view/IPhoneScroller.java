package com.nitrous.iosched.client.view;

/**
 *  Copyright (c) 2010 by John Beaven
 *
 *  This program is free software: you can redistribute it and/or modify
 *  it under the terms of the GNU Lesser General Public License as published by
 *  the Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *  GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU Lesser General Public License
 *  along with this program.  If not, see <http://www.gnu.org/licenses/>.
 *  
 *  More details available at http://code.google.com/p/gwt-iscroll/
 *
 *  Note: The original iScroll Javascript library is available from:
 *  http://cubiq.org/, and is copyright (c) 2009 Matteo Spinelli
 *  released under the MIT license
 */

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.Widget;

public class IPhoneScroller {
	private static boolean libIncluded = false;
	JavaScriptObject iScrollObj;

	public void refresh() {
		refresh(iScrollObj);
	}

	public void scrollTo(int position, int time_ms) {
		scrollTo(iScrollObj, String.valueOf(position), String.valueOf(time_ms) + "ms");
	}

	public void scrollTo(int position) {
		scrollTo(iScrollObj, String.valueOf(position), "300ms");
	}

	public int getPosition() {
		return getPosition(iScrollObj);
	}

	public void setPosition(int pos) {
		setPosition(iScrollObj, pos);
	}

	public IPhoneScroller(Widget w) {
		if (!libIncluded) {
			includeLibJSNI();
			libIncluded = true;
		}
		iScrollObj = makeScrollableJSNI(w.getElement());
	}

	protected native final void refresh(JavaScriptObject o) /*-{ 
		o.refresh(); 
	}-*/;

	protected native final void scrollTo(JavaScriptObject o, String position, String time_ms) /*-{ 
		o.scrollTo(position, time_ms); 
	}-*/;

	protected native final int getPosition(JavaScriptObject o) /*-{ 
		return o.position(); 
	}-*/;

	protected native final void setPosition(JavaScriptObject o, int pos) /*-{ 
			o.setposition(pos); 
	}-*/;

	protected native final JavaScriptObject makeScrollableJSNI(Element elem) /*-{ 
		return new iScroll(elem); 
	}-*/;

	protected native final void includeLibJSNI() /*-{
		
		// INCLUSION OF THE ORIGINAL iScroll LIBRARY..
		// 
		// Find more about the scrolling function at
		// http://cubiq.org/scrolling-div-on-iphone-ipod-touch/5
		//
		// Copyright (c) 2009 Matteo Spinelli, http://cubiq.org/
		// Released under MIT license
		// http://cubiq.org/dropbox/mit-license.txt
		// 
		// Version 2.3 - Last updated: 2009.07.09
 
		iScroll = function(el) {
			this.element = el;
			this.setposition(0);
			this.refresh();
			this.element.style.webkitTransitionTimingFunction = 'cubic-bezier(0, 0, 0.2, 1)';
			this.acceleration = 0.009;	
			this.element.addEventListener('touchstart', this, false);
		}
	
		iScroll.prototype = {
			handleEvent: function(e) {
				switch(e.type) {
				case 'touchstart': this.onTouchStart(e); break;
				case 'touchmove': this.onTouchMove(e); break;
				case 'touchend': this.onTouchEnd(e); break;
				case 'webkitTransitionEnd': this.onTransitionEnd(e); break;
				}
			},
	
			position: function() {
				return this._position;
			},
	
			setposition: function(pos) {
				this._position = pos;
				this.element.style.webkitTransform = 'translate3d(0, ' + this._position + 'px, 0)';
			},
	
			refresh: function() {
				this.element.style.webkitTransitionDuration = '0';
	
				if( this.element.offsetHeight<this.element.parentNode.clientHeight )
					this.maxScroll = 0;
				else		
					this.maxScroll = this.element.parentNode.clientHeight - this.element.offsetHeight;
			},
	
			onTouchStart: function(e) {
				e.preventDefault();
				this.element.style.webkitTransitionDuration = '0';	// Remove any transition
				var theTransform = window.getComputedStyle(this.element).webkitTransform;
				theTransform = new WebKitCSSMatrix(theTransform).m42;
				if( theTransform!=this.position() )
					this.setposition(theTransform);
	
				this.startY = e.targetTouches[0].clientY;
				this.scrollStartY = this.position();
				this.scrollStartTime = e.timeStamp;
				this.moved = false;
	
				this.element.addEventListener('touchmove', this, false);
				this.element.addEventListener('touchend', this, false);
	
				return false;
			},
	
			onTouchMove: function(e) {
				if( e.targetTouches.length != 1 )
					return false;
	
				var topDelta = e.targetTouches[0].clientY - this.startY;
				if( this.position()>0 || this.position()<this.maxScroll ) 
					topDelta/=2;
				this.setposition(this.position() + topDelta);
				this.startY = e.targetTouches[0].clientY;
				this.moved = true;
		
				// Prevent slingshot effect
				if( e.timeStamp-this.scrollStartTime>100 ) {
					this.scrollStartY = this.position();
					this.scrollStartTime = e.timeStamp;
				}
		
				return false;
			},
	
			onTouchEnd: function(e) {
				this.element.removeEventListener('touchmove', this, false);
				this.element.removeEventListener('touchend', this, false);
	
				// If we are outside of the boundaries, let's go back to the sheepfold
				if( this.position()>0 || this.position()<this.maxScroll ) {
					this.scrollTo(this.position()>0 ? 0 : this.maxScroll);
					return false;
				}
	
				if( !this.moved ) {
					var theTarget = e.target;
					if(theTarget.nodeType == 3) 
						theTarget = theTarget.parentNode;
					var theEvent = document.createEvent("MouseEvents");
					theEvent.initEvent('click', true, true);
					theTarget.dispatchEvent(theEvent);
					return false
				}
	
				// Lame formula to calculate a fake deceleration
				var scrollDistance = this.position() - this.scrollStartY;
				var scrollDuration = e.timeStamp - this.scrollStartTime;
		
				var newDuration = (2 * scrollDistance / scrollDuration) / this.acceleration;
				var newScrollDistance = (this.acceleration / 2) * (newDuration * newDuration);
		
				if( newDuration<0 ) {
					newDuration = -newDuration;
					newScrollDistance = -newScrollDistance;
				}
		
				var newPosition = this.position() + newScrollDistance;
		
				if( newPosition>this.element.parentNode.clientHeight/2 )
					newPosition = this.element.parentNode.clientHeight/2;
				else if( newPosition>0 )
					newPosition/= 1.5;
				else if( newPosition<this.maxScroll-this.element.parentNode.clientHeight/2 )
					newPosition = this.maxScroll-this.element.parentNode.clientHeight/2;
				else if( newPosition<this.maxScroll )
					newPosition = (newPosition - this.maxScroll) / 1.5 + this.maxScroll;
				else
					newDuration*= 6;
		
				this.scrollTo(newPosition, Math.round(newDuration) + 'ms');
		
				return false;
			},
	
			onTransitionEnd: function() {
				this.element.removeEventListener('webkitTransitionEnd', this, false);
				this.scrollTo( this.position()>0 ? 0 : this.maxScroll );
			},
	
			scrollTo: function(dest, runtime) {
				this.element.style.webkitTransitionDuration = runtime ? runtime : '300ms';
				this.setposition(dest ? dest : 0);
		
				// If we are outside of the boundaries at the end of the transition go back to the sheepfold
				if( this.position()>0 || this.position()<this.maxScroll )
					this.element.addEventListener('webkitTransitionEnd', this, false);
				}
			};	
		}-*/;
	
}
