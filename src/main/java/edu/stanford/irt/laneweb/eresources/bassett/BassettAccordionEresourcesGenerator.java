package edu.stanford.irt.laneweb.eresources.bassett;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;

/**
 * 
 * @author alainb
 *
 * $Id$
 */
public class BassettAccordionEresourcesGenerator extends AbstractBassettGenerator {

    public void generate() throws SAXException {
        Map<String, Integer> regionCountMap = this.collectionManager.searchCount(null, null, this.query);
        this.xmlConsumer.startDocument();
        XMLizable xml = new XMLLizableBassettCount(regionCountMap);
        xml.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = this.model.getString(LanewebObjectModel.QUERY, "bassett");
    }
}
