package edu.stanford.irt.laneweb.classes;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.excalibur.source.SourceValidity;

public class ClassesValidity implements SourceValidity {

    private static final long serialVersionUID = 1L;
    private static final long DELAY = 3600*1000;
    private long expires;
    
    
    
    public ClassesValidity() {
        this.expires = System.currentTimeMillis() + DELAY;
    }

    public int isValid() {
        final long now = System.currentTimeMillis();
        if (now <= this.expires) {
            return SourceValidity.VALID;
        }
        GregorianCalendar cal = new GregorianCalendar();
        int hour = cal.get(Calendar.HOUR_OF_DAY);
        if(hour == 23)
            return SourceValidity.INVALID;
        return SourceValidity.VALID;
    }

    public int isValid(SourceValidity newValidity){ 
        return SourceValidity.INVALID;
    }
}
