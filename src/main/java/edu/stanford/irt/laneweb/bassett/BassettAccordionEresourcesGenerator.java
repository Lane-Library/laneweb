package edu.stanford.irt.laneweb.bassett;

import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettAccordionEresourcesGenerator extends AbstractBassettGenerator {

    public void generate() {
        Map<String, Integer> regionCountMap = this.collectionManager.searchCount(null, null, this.query);
        XMLConsumer xmlConsumer = getXMLConsumer();
        try {
            xmlConsumer.startDocument();
            XMLizable xml = new XMLLizableBassettCount(regionCountMap);
            xml.toSAX(xmlConsumer);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    @Override
    protected void initialize() {
        this.query = ModelUtil.getString(getModel(), Model.QUERY, "bassett");
    }
}
