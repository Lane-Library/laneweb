package edu.stanford.irt.laneweb.bassett;

import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettAccordionEresourcesGenerator extends AbstractBassettGenerator {

    public void generate() throws SAXException {
        Map<String, Integer> regionCountMap = this.collectionManager.searchCount(null, null, this.query);
        XMLConsumer xmlConsumer = getXMLConsumer();
        xmlConsumer.startDocument();
        XMLizable xml = new XMLLizableBassettCount(regionCountMap);
        xml.toSAX(xmlConsumer);
        xmlConsumer.endDocument();
    }

    @Override
    protected void initialize() {
        this.query = ModelUtil.getString(getModel(), Model.QUERY, "bassett");
    }
}
