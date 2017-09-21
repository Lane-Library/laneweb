package edu.stanford.irt.laneweb.cocoon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private static final Logger log = LoggerFactory.getLogger(XIncludeExceptionListener.class);

    @Override
    public void exception(final Locator locator, final Exception e) {
        exception(null, null, null, locator, e);
    }

    @Override
    public void exception(String href, String parse, String xpointer, Locator locator, Exception e) {
        StringBuilder message = new StringBuilder("XInclude failed: ");
        message.append("href=")
        .append(href)
        .append(" parse=")
        .append(parse)
        .append(" xpointer=")
        .append(xpointer);
        if (locator != null) {
            message.append(" ")
            .append(locator.getSystemId())
            .append(" line:")
            .append(locator.getLineNumber())
            .append(" column:")
            .append(locator.getColumnNumber());
        }
        message.append(": ").append(e);
        log.error(message.toString());
    }
}
