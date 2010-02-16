package edu.stanford.irt.laneweb.eresources.bassett;

import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
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

    private static final String BASSETT_NUMBER = "bn";

    private static final String REGION = "region";

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

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = getString(LanewebObjectModel.QUERY);
        this.region = par.getParameter(REGION, null);
        this.bassettNumber = par.getParameter(BASSETT_NUMBER, null);
    }
}
