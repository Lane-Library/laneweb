package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.xml.sax.XMLizable;
import org.apache.log4j.Logger;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.Eresource;

public class BassettEresourcesGenerator implements Generator {
    
    private Logger logger = Logger.getLogger(BassettEresourcesGenerator.class);

    private static final String QUERY = "q";
    private static final String REGION = "r";
    private static final String BASSETT_NUMBER = "bn";
    private static final String REGION_COUNT = "region-count";

    private BassettCollectionManager collectionManager;

    private XMLConsumer xmlConsumer;

    private String query;
    private String region;
    private String bassettNumber;
    private String region_count;

    public void setCollectionManager(final BassettCollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        if (logger.isDebugEnabled()) {
            logger.debug("setup");
        }
        Request request = ObjectModelHelper.getRequest(objectModel);
        String query = request.getParameter(QUERY);
        this.region = request.getParameter(REGION);
        this.bassettNumber = request.getParameter(BASSETT_NUMBER);
        if (null != query) {
            this.query = query.trim();
            if (this.query.length() == 0) {
                this.query = null;
            }
        }
        this.region_count = request.getParameter(REGION_COUNT);
    }

    public void generate() throws SAXException {
        if (logger.isDebugEnabled()) {
            logger.debug("generate");
        }
        Collection<Eresource> eresources = null;
        Map<String, Integer> regionCountMap = null;
        if (this.region_count != null) {
            if (this.query == null) {
                this.query = "bassett";
            }
            regionCountMap = this.collectionManager.searchCount(null, null, this.query);
        } else if (this.bassettNumber != null) {
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

        XMLizable xml = null;
        this.xmlConsumer.startDocument();
        if (regionCountMap != null) {
            xml = new XMLLizableBassettCount(regionCountMap);

        } else {
            xml = new XMLLizableBassettEresourceList(eresources);
        }
        xml.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }

}
