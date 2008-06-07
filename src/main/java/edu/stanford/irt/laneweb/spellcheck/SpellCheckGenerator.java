/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import java.io.IOException;
import java.util.Map;

import org.apache.avalon.framework.activity.Disposable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.SAXException;

import edu.stanford.irt.spell.SpellCheckResult;
import edu.stanford.irt.spell.SpellChecker;

/**
 * @author ceyates
 * 
 */
public class SpellCheckGenerator implements Generator, Serviceable, Disposable,
        ThreadSafe {

    private static final String SPELLCHECK = "spellcheck";

    private static final String QUERY = "query";

    private static final String SUGGESTION = "suggestion";

    private static final String NAMESPACE = "http://lane.stanford.edu/spellcheck/ns";

    private SpellChecker spellChecker;

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private ThreadLocal<XMLConsumer> xmlConsumer = new ThreadLocal<XMLConsumer>();

    private ServiceManager serviceManager;

    public void dispose() {
        if (null == this.spellChecker) {
            return;
        }
        if (null == this.serviceManager) {
            throw new IllegalStateException("null serviceManager");
        }
        this.serviceManager.release(this.spellChecker);
    }

    public void service(final ServiceManager serviceManager)
            throws ServiceException {
        if (null == serviceManager) {
            throw new IllegalArgumentException("null serviceManager");
        }
        this.serviceManager = serviceManager;
        this.spellChecker = (SpellChecker) this.serviceManager
                .lookup(SpellChecker.class.getName());
    }

    public void generate() throws IOException, SAXException,
            ProcessingException {
        String query = this.query.get();
        if (null == query) {
            this.xmlConsumer.set(null);
            throw new IllegalStateException("null query");
        }
        XMLConsumer xmlConsumer = this.xmlConsumer.get();
        if (null == xmlConsumer) {
            this.query.set(null);
            throw new IllegalStateException("null xmlConsumer");
        }
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, NAMESPACE, SPELLCHECK);
            if ((null != query) && (query.length() > 0)) {
                SpellCheckResult result = this.spellChecker.spellCheck(query);
                XMLUtils.createElementNS(xmlConsumer, NAMESPACE, QUERY, query);
                if (null != result.getSuggestion()) {
                    XMLUtils.createElementNS(xmlConsumer, NAMESPACE,
                            SUGGESTION, result.getSuggestion());
                }
            }
            XMLUtils.endElement(xmlConsumer, NAMESPACE, SPELLCHECK);
            xmlConsumer.endDocument();
        } finally {
            this.query.set(null);
            this.xmlConsumer.set(null);
        }
    }

    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters params) {
        if (null == params) {
            throw new IllegalArgumentException("null params");
        }
        String param = params.getParameter(QUERY, null);
        if (null == param) {
            throw new IllegalArgumentException("null query");
        }
        this.query.set(param);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer.set(xmlConsumer);
    }

}
