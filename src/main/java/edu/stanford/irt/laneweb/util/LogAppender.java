package edu.stanford.irt.laneweb.util;

import java.io.FileNotFoundException;

import org.apache.log4j.RollingFileAppender;
import org.apache.log4j.spi.LoggingEvent;
import org.apache.log4j.spi.ThrowableInformation;

/**
 * A log appender that doesn't spew out error stack traces in the log just
 * because someone requested a file that doesn't exist.
 */
public class LogAppender extends RollingFileAppender {

    @Override
    public synchronized void doAppend(final LoggingEvent event) {
        ThrowableInformation ti = event.getThrowableInformation();
        if (null != ti) {
            Throwable t = ti.getThrowable();
            while (t.getCause() != null) {
                t = t.getCause();
            }
            if (t instanceof FileNotFoundException) {
                LoggingEvent newEvent = new LoggingEvent(event.fqnOfCategoryClass, event.getLogger(), event.getLevel(),
                        t.toString(), null);
                super.doAppend(newEvent);
                return;
            }
        }
        super.doAppend(event);
    }
}
