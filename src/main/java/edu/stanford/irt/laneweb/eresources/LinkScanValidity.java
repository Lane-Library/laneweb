package edu.stanford.irt.laneweb.eresources;

import edu.stanford.irt.cocoon.cache.Validity;

public class LinkScanValidity implements Validity {

    // no need to query solr more than hourly
    private static final int CHECK_INTERVAL = 1000 * 60 * 60;

    private static final long serialVersionUID = 1L;

    private long lastCheck = 0;

    public LinkScanValidity() {
        this.lastCheck = System.currentTimeMillis();
    }

    // for unit testing
    protected LinkScanValidity(final long lastCheck) {
        this.lastCheck = lastCheck;
    }

    @Override
    public boolean isValid() {
        boolean valid = true;
        long now = System.currentTimeMillis();
        if (now > this.lastCheck + CHECK_INTERVAL) {
            this.lastCheck = now;
            valid = false;
        }
        return valid;
    }
}
