package edu.stanford.irt.laneweb.classes;

import java.util.Calendar;
import java.util.GregorianCalendar;

import edu.stanford.irt.cocoon.source.SourceValidity;

public class ClassesValidity implements SourceValidity {

    private static final long DELAY = 3600 * 1000;

    private static final long serialVersionUID = 1L;

    private long expires;

    public ClassesValidity() {
        this.expires = System.currentTimeMillis() + DELAY;
    }

    public boolean isValid() {
        final long now = System.currentTimeMillis();
        if (now <= this.expires) {
            return true;
        }
        GregorianCalendar cal = new GregorianCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if (hour == 23) {
            return false;
        }
        return true;
    }
}
