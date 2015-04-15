package edu.stanford.irt.laneweb.cocoon;

import org.slf4j.Logger;
import org.xml.sax.Locator;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private Logger log;

    public XIncludeExceptionListenerImpl(final Logger log) {
        this.log = log;
    }

    @Override
    public void exception(final Locator locator, final Exception e) {
        if (locator == null) {
            this.log.error(e.getMessage(), e);
        } else {
            this.log.error(createMessage(locator), e);
        }
    }

    private String createMessage(final Locator locator) {
        return new StringBuilder("XInclude failed: ").append(locator.getSystemId()).append(" line:")
                .append(locator.getLineNumber()).append(" column:").append(locator.getColumnNumber()).toString();
    }
}
