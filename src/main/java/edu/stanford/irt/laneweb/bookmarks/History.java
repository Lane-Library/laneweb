package edu.stanford.irt.laneweb.bookmarks;

import java.util.Date;

import edu.stanford.irt.laneweb.LanewebException;

public class History extends Bookmark {

    private static final long serialVersionUID = 1L;

    private Date date;

    private transient int hashcode = 0;

    public History(final String label, final String url, final Date date) {
        super(label, url);
        if (date == null) {
            throw new LanewebException("null date");
        }
        this.date = date;
    }

    @Override
    public boolean equals(final Object other) {
        boolean equals = false;
        if (other instanceof History && other.hashCode() == hashCode()) {
            History that = (History) other;
            equals = this.date.equals(that.date) && super.equals(that);
        }
        return equals;
    }

    @Override
    public int hashCode() {
        if (this.hashcode == 0) {
            this.hashcode = super.hashCode() ^ this.date.hashCode();
        }
        return this.hashcode;
    }
}
