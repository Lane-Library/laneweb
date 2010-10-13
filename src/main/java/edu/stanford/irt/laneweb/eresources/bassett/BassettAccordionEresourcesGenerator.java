package edu.stanford.irt.laneweb.eresources.bassett;

import java.util.Map;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author alainb $Id$
 */
public class BassettAccordionEresourcesGenerator extends AbstractBassettGenerator {

    public void generate() throws SAXException {
        Map<String, Integer> regionCountMap = this.collectionManager.searchCount(null, null, this.query);
        this.xmlConsumer.startDocument();
        XMLizable xml = new XMLLizableBassettCount(regionCountMap);
        xml.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    @Override
    protected void initialize() {
        this.query = getString(this.model, Model.QUERY, "bassett");
    }
}
