package edu.stanford.irt.laneweb.cocoon;

import javax.xml.transform.sax.TransformerHandler;

import org.apache.avalon.framework.logger.CommonsLogger;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceResolver;
import org.apache.excalibur.store.Store;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.apache.excalibur.xmlizer.XMLizer;
import org.xml.sax.XMLFilter;

public class XSLTProcessorImpl extends org.apache.excalibur.xml.xslt.XSLTProcessorImpl {

    private static final Log LOGGER = LogFactory.getLog(XSLTProcessorImpl.class);

    public XSLTProcessorImpl(final XMLizer xmlizer, final Store store, final SourceResolver sourceResolver) {
        enableLogging(new CommonsLogger(LOGGER, getClass().getName()));
        this.m_xmlizer = xmlizer;
        this.m_store = store;
        this.m_resolver = sourceResolver;
        this.m_useStore = true;
    }

    @Override
    public void dispose() {
        throw new UnsupportedOperationException();
    }

    @Override
    public TransformerHandler getTransformerHandler(final Source stylesheet, final XMLFilter filter)
            throws XSLTProcessorException {
        if (filter != null) {
            throw new UnsupportedOperationException();
        }
        return super.getTransformerHandler(stylesheet, null);
    }

    @Override
    public void parameterize(final Parameters params) {
        throw new UnsupportedOperationException();
    }

    @Override
    public void recycle() {
        throw new UnsupportedOperationException();
    }

    @Override
    public void service(final ServiceManager manager) {
        throw new UnsupportedOperationException();
    }
}
