package edu.stanford.irt.laneweb.bookmarks;

import java.io.Serializable;

public class Bookmark implements Serializable {

	private static final long serialVersionUID = 1L;

	private String label;

	private String url;

	protected Bookmark() {
	}

	public Bookmark(final String label, final String url) {
		this.label = label;
		this.url = url;
	}

	public String getLabel() {
		return this.label;
	}

	public String getUrl() {
		return this.url;
	}

	public void setLabel(final String label) {
		this.label = label;
	}

	public void setUrl(final String url) {
		this.url = url;
	}
	
	@Override
	public int hashCode() {
		int hashcode;
		if (this.label != null && this.url != null) {
			hashcode = this.label.hashCode() + this.url.hashCode();
		} else if (this.label != null) {
			hashcode = this.label.hashCode();
		} else if (this.url != null) {
			hashcode = this.url.hashCode();
		} else {
			hashcode = super.hashCode();
		}
		return hashcode;
	}

	@Override
	public boolean equals(Object other) {
		boolean equals = false;
		if (other instanceof Bookmark) {
			Bookmark bookmark = (Bookmark) other;
			if (this.label == null && this.url == null) {
				equals = bookmark.label == null && bookmark.url == null;
			} else if (this.label == null) {
				equals = bookmark.label == null && this.url.equals(bookmark.url);
			} else if (this.url == null) {
				equals = bookmark.url == null && this.label.equals(bookmark.label);
			} else {
				equals = this.label.equals(bookmark.label) && this.url.equals(bookmark.url);
			}
		}
		return equals;
	}
}
