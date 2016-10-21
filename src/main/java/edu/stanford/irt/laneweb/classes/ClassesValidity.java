package edu.stanford.irt.laneweb.classes;

import java.util.Calendar;

import edu.stanford.irt.cocoon.cache.Validity;

public class ClassesValidity implements Validity {

    private static final int ELEVEN_PM = 23;

    private static final int ONE_HOUR_DELAY = 3600 * 1000;

    private static final long serialVersionUID = 1L;

    private int hour;

    private long expires;

    public ClassesValidity() {
        this(ELEVEN_PM, ONE_HOUR_DELAY);
    }

    public ClassesValidity(final int hour, final long delay) {
        this.hour = hour;
        this.expires = System.currentTimeMillis() + delay;
    }

    @Override
    public boolean isValid() {
        final long now = System.currentTimeMillis();
        if (now <= this.expires) {
            return true;
        }
        if (Calendar.getInstance().get(Calendar.HOUR_OF_DAY) == this.hour) {
            return false;
        }
        return true;
    }
}
