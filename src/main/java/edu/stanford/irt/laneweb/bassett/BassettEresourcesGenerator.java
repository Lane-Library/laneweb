package edu.stanford.irt.laneweb.bassett;

import java.util.Collection;

import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

/**
 * @author alainb $Id$
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

    @Override
    protected void initialize() {
        this.query = ModelUtil.getString(this.model, Model.QUERY);
        this.region = ModelUtil.getString(this.model, Model.REGION);
        this.bassettNumber = ModelUtil.getString(this.model, Model.BASSETT_NUMBER);
    }
}
