package com.nitrous.iosched.client.view;

import java.util.ArrayList;
public class Bookmark {
	private BookmarkCategory category;
	private ArrayList<String> stateTokens;
	public Bookmark(BookmarkCategory category) {
		this(category, null);
	}
	public Bookmark(BookmarkCategory category, ArrayList<String> stateTokens) {
		this.stateTokens = stateTokens;
		this.category = category;
	}
	
	public BookmarkCategory getCategory() {
		return category;
	}
	public void clearStateTokens() {
		if (stateTokens != null) {
			stateTokens.clear();
		}
	}
	public boolean hasState() {
		return stateTokens != null && stateTokens.size() > 0;
	}
	public ArrayList<String> getState() {
		return stateTokens;
	}
	public void setStateToken(String token) {
		if (stateTokens != null) {
			stateTokens.clear();
		} else {
			stateTokens = new ArrayList<String>();
		}
		stateTokens.add(token);
	}
	public void addStateToken(String token) {
		if (stateTokens == null) {
			stateTokens = new ArrayList<String>();
		}
		stateTokens.add(token);
	}
	/**
	 * @Return The URL encoded bookmark
	 */
	public String toString() {
		StringBuilder buf = new StringBuilder();
		buf.append(category.toString());
		if (stateTokens != null && stateTokens.size() > 0) {
			for (String s : stateTokens) {
				buf.append(";");
				buf.append(s);
			}
		}
		return buf.toString();
	}
	public static Bookmark parse(String token) {
		if (token != null) {
			token = token.trim();
			if (token.length() > 0) {
				String[] parts = token.split(";");
				if (parts.length > 0) {
					String categoryStr = parts[0];
					BookmarkCategory cat = null;
					for (BookmarkCategory b : BookmarkCategory.values()) {
						if (b.toString().equalsIgnoreCase(categoryStr)) {
							cat = b;
							break;
						}
					}
					if (cat != null) {
						ArrayList<String> state = new ArrayList<String>();
						if (parts.length > 1) {
							for (int i = 1; i < parts.length; i++) {
								state.add(parts[i]);
							}
						}
						return new Bookmark(cat, state);
					}
				}
			}			
		}
		return null;
	}
}
