package edu.stanford.irt.laneweb.eresources.bassett;

import java.util.Collection;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

/**
 * 
 * @author alainb
 *
 * $Id$
 */
public class BassettEresourcesGenerator extends AbstractBassettGenerator {

    private String bassettNumber;

    private String query;

    private String region;

    public void generate() throws SAXException {
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
        this.xmlConsumer.startDocument();
        XMLizable xml = new XMLLizableBassettEresourceList(eresources);
        xml.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void initialize() {
        this.query = this.model.getString(LanewebObjectModel.QUERY);
        this.region = this.model.getString(LanewebObjectModel.REGION);
        this.bassettNumber = this.model.getString(LanewebObjectModel.BASSETT_NUMBER);
    }
}
