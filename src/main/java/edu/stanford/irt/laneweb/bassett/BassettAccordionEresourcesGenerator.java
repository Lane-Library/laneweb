package edu.stanford.irt.laneweb.bassett;

import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettAccordionEresourcesGenerator extends AbstractBassettGenerator implements ModelAware {

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY, "bassett");
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Map<String, Integer> regionCountMap = this.collectionManager.searchCount(this.query);
        try {
            xmlConsumer.startDocument();
            XMLLizableBassettCount xml = new XMLLizableBassettCount(regionCountMap);
            xml.toSAX(xmlConsumer);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
