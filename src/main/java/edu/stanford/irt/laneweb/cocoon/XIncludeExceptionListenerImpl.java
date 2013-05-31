package edu.stanford.irt.laneweb.cocoon;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xml.sax.Locator;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private Logger log = LoggerFactory.getLogger(XIncludeExceptionListener.class);

    public void exception(Locator locator, final Exception e) {
        this.log.error(createMessage(locator), e);
    }
    
    private String createMessage(Locator locator) {
        return new StringBuilder("XInclude failed: ")
        .append(locator.getSystemId())
        .append(" line:")
        .append(locator.getLineNumber())
        .append(" column:")
        .append(locator.getColumnNumber())
        .toString();
    }
}
