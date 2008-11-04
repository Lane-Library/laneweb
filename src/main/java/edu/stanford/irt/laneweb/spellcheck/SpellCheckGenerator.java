/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.spell.SpellChecker;

/**
 * @author ceyates
 */
public class SpellCheckGenerator implements Generator {

    private static final String QUERY = "query";

    private SpellChecker spellChecker;

    private ThreadLocal<String> query = new ThreadLocal<String>();

    private ThreadLocal<XMLConsumer> consumer = new ThreadLocal<XMLConsumer>();

    public void setSpellChecker(final SpellChecker spellChecker) {
        if (null == spellChecker) {
            throw new IllegalArgumentException("null spellChecker");
        }
        this.spellChecker = spellChecker;
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

        try {

            XMLizable result = new XMLizableSpellCheckResult(query, this.spellChecker.spellCheck(query));

            consumer.startDocument();
            result.toSAX(consumer);
            consumer.endDocument();
        } finally {
            this.consumer.set(null);
            this.query.set(null);
        }
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
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
