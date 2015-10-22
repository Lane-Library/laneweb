package edu.stanford.irt.laneweb.bookmarks;

import java.io.Serializable;
import java.util.Objects;

import edu.stanford.irt.laneweb.LanewebException;

/**
 * The Bookmark object, having a label which corresponds to the link text, and a url which corresponds to the link's
 * href attribute. It is a pseudo-immutable object in that the label and url can be changed but only if they have never
 * been set. This is so the jackson-mapper library can create one with the no parameter constructor then set the label
 * and url.
 */
public class Bookmark implements Serializable {

    private static final long serialVersionUID = 1L;

    private transient int hashcode = 0;

    private String label;

    private String url;

    /**
     * Create a new Bookmark with null label and url.
     */
    public Bookmark() {
        // default empty constructor
    }

    /**
     * Create a new Bookmark.
     *
     * @param label
     *            the text associated with the bookmark
     * @param url
     *            the url associated with the bookmark
     * @throws NullPointerException
     *             if either the label or url are null.
     */
    public Bookmark(final String label, final String url) {
        this.label = Objects.requireNonNull(label, "null label");
        this.url = Objects.requireNonNull(url, "null url");
    }

    /**
     * Uses the hashCode() and equals() from the label and url.
     *
     * @return true if url and label are the same in both Bookmarks
     */
    @Override
    public boolean equals(final Object other) {
        boolean equals = false;
        if (other instanceof Bookmark && other.hashCode() == hashCode()) {
            Bookmark that = (Bookmark) other;
            equals = this.label.equals(that.label) && this.url.equals(that.url);
        }
        return equals;
    }

    /**
     * Getter for the label.
     *
     * @return the label
     */
    public String getLabel() {
        return this.label;
    }

    /**
     * Getter for the url.
     *
     * @return the url
     */
    public String getUrl() {
        return this.url;
    }

    /**
     * @return Objects.hash(this.label, this.url);
     */
    @Override
    public int hashCode() {
        if (this.hashcode == 0) {
            this.hashcode = Objects.hash(this.label, this.url);
        }
        return this.hashcode;
    }

    /**
     * Set the label.
     *
     * @param label
     *            the text associated with the bookmark
     * @throws LanewebException
     *             if the label has already been set.
     * @throws NullPointerException
     *             if the label is null.
     */
    public void setLabel(final String label) {
        if (this.label != null) {
            throw new LanewebException("cannot change label");
        }
        this.label = Objects.requireNonNull(label, "null label");
    }

    /**
     * Set the url
     *
     * @param url
     *            the url associated with the bookmark
     * @throws LanewebException
     *             if the url has already been set.
     * @throws NullPointerException
     *             if the url is null.
     */
    public void setUrl(final String url) {
        if (this.url != null) {
            throw new LanewebException("cannot change url");
        }
        this.url = Objects.requireNonNull(url, "null url");
    }

    @Override
    public String toString() {
        return this.label + "=" + this.url;
    }
}
