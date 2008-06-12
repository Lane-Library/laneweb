package edu.stanford.irt.laneweb.spellcheck;

import org.apache.cocoon.xml.XMLUtils;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

import edu.stanford.irt.spell.SpellCheckResult;

public class XMLizableSpellCheckResult implements XMLizable {

    private static final String SPELLCHECK = "spellcheck";

    private static final String QUERY = "query";

    private static final String SUGGESTION = "suggestion";

    private static final String NAMESPACE = "http://lane.stanford.edu/spellcheck/ns";

    private SpellCheckResult result;

    private String query;

    public XMLizableSpellCheckResult(final String query,
            final SpellCheckResult result) {
        this.query = query;
        this.result = result;
    }

    public void toSAX(final ContentHandler consumer) throws SAXException {
        XMLUtils.startElement(consumer, NAMESPACE, SPELLCHECK);
        XMLUtils.createElementNS(consumer, NAMESPACE, QUERY, this.query);
        if (null != this.result.getSuggestion()) {
            XMLUtils.createElementNS(consumer, NAMESPACE, SUGGESTION,
                    this.result.getSuggestion());
        }
        XMLUtils.endElement(consumer, NAMESPACE, SPELLCHECK);
    }

}
