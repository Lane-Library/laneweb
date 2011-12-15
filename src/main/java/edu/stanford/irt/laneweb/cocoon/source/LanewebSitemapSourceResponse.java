package edu.stanford.irt.laneweb.cocoon.source;

import edu.stanford.irt.laneweb.cocoon.expression.LanewebResponse;

public class LanewebSitemapSourceResponse extends LanewebResponse {

    public LanewebSitemapSourceResponse() {
        super(null);
    }

    @Override
    public void setDateHeader(final String name, final long date) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setHeader(final String name, final String value) {
        throw new UnsupportedOperationException();
    }
}
