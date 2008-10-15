package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.activity.Initializable;
import org.apache.avalon.framework.parameters.ParameterException;
import org.apache.avalon.framework.parameters.Parameterizable;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.apache.excalibur.xml.sax.XMLizable;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class BassettEresourcesGenerator extends ServiceableGenerator implements
        Parameterizable, Initializable {

    private static final String QUERY = "q";
    private static final String REGION = "r";
    private static final String BASSETT_NUMBER = "bn";
    private static final String REGION_COUNT = "region-count";

    private BassettCollectionManager collectionManager;

    private String query;
    private String region;
    private String collection;
    private String bassettNumber;
    private String region_count;

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = (BassettCollectionManager) collectionManager;
    }

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
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
        Collection<Eresource> eresources = null;
        Map regionCountMap = null;
        if (this.region_count != null) {
            if (this.query == null) {
                this.query = "bassett";
            }
            regionCountMap = this.collectionManager.searchCount(null, null,
                    this.query);
        }

        if (this.bassettNumber != null) {
            eresources = this.collectionManager.getById(this.bassettNumber);
        } else if (this.region != null) {
            if (this.query != null) {
                eresources = this.collectionManager.searchSubset(this.region,
                        this.query);
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

    @Override
    public void recycle() {
        this.query = null;
    }

    @Override
    public void dispose() {
        this.manager.release(this.collectionManager);
        super.dispose();
    }

    public void parameterize(final Parameters param) throws ParameterException {
        this.collection = param.getParameter("collection", "laneconnex");
    }

    public void initialize() throws ServiceException {
        setCollectionManager((CollectionManager) this.manager
                .lookup(CollectionManager.class.getName() + "/"
                        + this.collection));
    }

}
