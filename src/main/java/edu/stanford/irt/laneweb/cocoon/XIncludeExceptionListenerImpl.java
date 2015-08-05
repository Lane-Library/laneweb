package edu.stanford.irt.laneweb.cocoon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private static final Logger LOG = LoggerFactory.getLogger(XIncludeExceptionListener.class);

    @Override
    public void exception(final Locator locator, final Exception e) {
        if (locator == null) {
            LOG.error(e.getMessage(), e);
        } else {
            LOG.error(createMessage(locator), e);
        }
    }

    private String createMessage(final Locator locator) {
        return new StringBuilder("XInclude failed: ").append(locator.getSystemId()).append(" line:")
                .append(locator.getLineNumber()).append(" column:").append(locator.getColumnNumber()).toString();
    }
}
