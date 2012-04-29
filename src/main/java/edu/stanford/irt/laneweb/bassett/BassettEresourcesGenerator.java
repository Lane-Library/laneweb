package edu.stanford.irt.laneweb.bassett;

import java.util.Collection;
import java.util.Map;

import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.cocoon.ModelAware;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb
 */
public class BassettEresourcesGenerator extends AbstractBassettGenerator implements ModelAware {

    private String bassettNumber;

    private String query;

    private String region;

    public void generate() {
        Collection<Eresource> eresources = null;
        if (this.bassettNumber != null) {
            eresources = this.collectionManager.getById(this.bassettNumber);
        } else if (this.region != null) {
            if (this.query != null) {
                eresources = this.collectionManager.searchSubset(this.region, this.query);
            } else {
                eresources = this.collectionManager.getSubset(this.region);
            }
        } else if (this.query != null) {
            eresources = this.collectionManager.search(this.query);
        }
        XMLConsumer xmlConsumer = getXMLConsumer();
        try {
            xmlConsumer.startDocument();
            XMLizable xml = new XMLLizableBassettEresourceList(eresources);
            xml.toSAX(xmlConsumer);
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    public void setModel(final Map<String, Object> model) {
        this.query = ModelUtil.getString(model, Model.QUERY);
        this.region = ModelUtil.getString(model, Model.REGION);
        this.bassettNumber = ModelUtil.getString(model, Model.BASSETT_NUMBER);
    }
}
