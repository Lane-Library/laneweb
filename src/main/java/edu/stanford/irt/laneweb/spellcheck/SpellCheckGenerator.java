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

import edu.stanford.irt.laneweb.model.DefaultModelAware;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.spell.SpellChecker;

/**
 * @author ceyates
 */
public class SpellCheckGenerator extends DefaultModelAware implements Generator {

    private XMLConsumer consumer;

    private String query;

    private SpellChecker spellChecker;

    public void generate() throws SAXException {
        if (null == this.consumer) {
            throw new IllegalStateException("null consumer");
        }
        XMLizable result = new XMLizableSpellCheckResult(this.query, this.spellChecker.spellCheck(this.query));
        this.consumer.startDocument();
        result.toSAX(this.consumer);
        this.consumer.endDocument();
    }

    public void setConsumer(final XMLConsumer consumer) {
        if (null == consumer) {
            throw new IllegalArgumentException("null consumer");
        }
        this.consumer = consumer;
    }

    public void setSpellChecker(final SpellChecker spellChecker) {
        if (null == spellChecker) {
            throw new IllegalArgumentException("null spellChecker");
        }
        this.spellChecker = spellChecker;
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters params) {
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}
