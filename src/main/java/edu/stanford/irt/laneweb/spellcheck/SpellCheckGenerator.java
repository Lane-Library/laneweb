/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.avalon.framework.service.Serviceable;
import org.apache.avalon.framework.thread.ThreadSafe;
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
public class SpellCheckGenerator implements Generator, Serviceable, ThreadSafe {

    private static final String SPELLCHECK = "spellcheck";

    private static final String QUERY = "query";

    private static final String SUGGESTION = "suggestion";

    private static final String NAMESPACE = "http://lane.stanford.edu/spellcheck/ns";

    private SpellChecker spellChecker;

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private ThreadLocal<XMLConsumer> consumer = new ThreadLocal<XMLConsumer>();
    
    public void setSpellChecker(final SpellChecker spellChecker) {
    	if (null == spellChecker) {
    		throw new IllegalArgumentException("null spellChecker");
    	}
    	this.spellChecker = spellChecker;
    }

    public void service(final ServiceManager serviceManager)
            throws ServiceException {
        if (null == serviceManager) {
            throw new IllegalArgumentException("null serviceManager");
        }
        setSpellChecker((SpellChecker) serviceManager.lookup(SpellChecker.class.getName()));
    }

    public void generate() throws SAXException {
        String query = this.query.get();
        if (null == query) {
            this.consumer.set(null);
            throw new IllegalStateException("null query");
        }
        XMLConsumer consumer = this.consumer.get();
        if (null == consumer) {
            this.query.set(null);
            throw new IllegalStateException("null consumer");
        }
        consumer.startDocument();
        XMLUtils.startElement(consumer, NAMESPACE, SPELLCHECK);
        if ((null != query) && (query.length() > 0)) {
            SpellCheckResult result = this.spellChecker.spellCheck(query);
            XMLUtils.createElementNS(consumer, NAMESPACE, QUERY, query);
            if (null != result.getSuggestion()) {
                XMLUtils.createElementNS(consumer, NAMESPACE, SUGGESTION,
                        result.getSuggestion());
            }
        }
        XMLUtils.endElement(consumer, NAMESPACE, SPELLCHECK);
        consumer.endDocument();
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

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer.set(consumer);
    }

}
