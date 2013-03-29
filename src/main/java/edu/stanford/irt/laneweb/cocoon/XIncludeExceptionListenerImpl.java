package edu.stanford.irt.laneweb.cocoon;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.cocoon.xml.XIncludeExceptionListener;

public class XIncludeExceptionListenerImpl implements XIncludeExceptionListener {

    private Logger log = LoggerFactory.getLogger(XIncludeExceptionListener.class);

    private Map<String, Object> model;

    public void exception(final Exception e) {
        this.log.error(this.model.toString(), e);
    }

    public void setModel(final Map<String, Object> model) {
        this.model = model;
    }
}
