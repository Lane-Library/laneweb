package edu.stanford.irt.laneweb.cocoon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private static final Logger log = LoggerFactory.getLogger(XIncludeExceptionListener.class);

    @Override
    public void exception(final Locator locator, final Exception e) {
        StringBuilder message = new StringBuilder("XInclude failed: ");
        if (locator != null) {
            message.append(locator.getSystemId())
            .append(" line:")
            .append(locator.getLineNumber())
            .append(" column:")
            .append(locator.getColumnNumber())
            .append(": ");
        }
        message.append(e.toString());
        log.error(message.toString());
    }
}
