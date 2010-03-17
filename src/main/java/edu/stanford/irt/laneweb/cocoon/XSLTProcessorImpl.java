package edu.stanford.irt.laneweb.cocoon;

import org.apache.avalon.framework.logger.CommonsLogger;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xmlizer.XMLizer;


public class XSLTProcessorImpl extends org.apache.excalibur.xml.xslt.XSLTProcessorImpl {
    
    private static final Log LOGGER = LogFactory.getLog(XSLTProcessorImpl.class);
    
    public XSLTProcessorImpl() {
        enableLogging(new CommonsLogger(LOGGER, getClass().getName()));
        this.m_useStore = true;
    }
    
    public void setStore(Store store) {
        this.m_store = store;
    }
    
    public void setXmlizer(XMLizer xmlizer) {
        this.m_xmlizer = xmlizer;
    }
}
