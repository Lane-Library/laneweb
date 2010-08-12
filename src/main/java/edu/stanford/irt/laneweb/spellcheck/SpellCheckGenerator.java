/**
 * 
 */
package edu.stanford.irt.laneweb.spellcheck;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.AbstractGenerator;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.spell.SpellChecker;

/**
 * @author ceyates
 */
public class SpellCheckGenerator extends AbstractGenerator {

    private String query;

    private SpellChecker spellChecker;

    public void generate() throws SAXException {
        if (null == this.xmlConsumer) {
            throw new IllegalStateException("null consumer");
        }
        XMLizable result = new XMLizableSpellCheckResult(this.query, this.spellChecker.spellCheck(this.query));
        this.xmlConsumer.startDocument();
        result.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setSpellChecker(final SpellChecker spellChecker) {
        if (null == spellChecker) {
            throw new IllegalArgumentException("null spellChecker");
        }
        this.spellChecker = spellChecker;
    }

    @Override
    protected void initialize() {
        this.query = this.model.getString(Model.QUERY);
        if (null == this.query) {
            throw new IllegalArgumentException("null query");
        }
    }
}
