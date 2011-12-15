package edu.stanford.irt.laneweb.cocoon.source;

import edu.stanford.irt.laneweb.cocoon.expression.LanewebRequest;

public class LanewebSitemapSourceRequest extends LanewebRequest {

    public LanewebSitemapSourceRequest(final String sitemapURI) {
        super(sitemapURI, null);
    }

    @Override
    public String getParameter(final String name) {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getQueryString() {
        throw new UnsupportedOperationException();
    }

    @Override
    public String getRequestURI() {
        throw new UnsupportedOperationException();
    }
}
