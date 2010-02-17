package edu.stanford.irt.laneweb.eresources;

import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.laneweb.model.DefaultObjectModelAware;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.searchresults.XMLizableSearchResultsList;

public abstract class AbstractEresourcesGenerator extends DefaultObjectModelAware implements Generator {

    private static final String ALPHA = "alpha";

    private static final String MESH = "mesh";

    private static final String TYPE = "type";
    
    private XMLConsumer xmlConsumer;

    protected String alpha;

    protected CollectionManager collectionManager;

    protected String mesh;

    protected String subset;

    protected String type;

    protected String query;
    
    public void generate() throws SAXException {
        XMLizableSearchResultsList eresources = new XMLizableSearchResultsList();
        eresources.setQuery(this.query);
        eresources.setEresources(getEresourceList());
        this.xmlConsumer.startDocument();
        eresources.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }
    
    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par) {
        this.query = getString(LanewebObjectModel.QUERY);
        this.type = par.getParameter(TYPE, null);
        if (null != this.type && this.type.length() == 0) {
            this.type = null;
        }
        this.subset = par.getParameter(LanewebObjectModel.SUBSET, getString(LanewebObjectModel.SUBSET));
        this.alpha = par.getParameter(ALPHA, null);
        if (this.alpha != null && this.alpha.length() == 0) {
            this.alpha = null;
        }
        this.mesh = par.getParameter(MESH, null);
        if (this.mesh != null) {
            if (this.mesh.length() == 0) {
                this.mesh = null;
            } else {
                this.mesh = this.mesh.toLowerCase();
            }
        }
    }

    protected abstract Collection<Eresource> getEresourceList();
}
