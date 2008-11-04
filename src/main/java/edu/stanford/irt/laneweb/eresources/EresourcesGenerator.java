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
import org.xml.sax.SAXException;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class EresourcesGenerator implements Generator {

    private static final String QUERY = "q";

    private static final String TYPE = "t";

    private static final String SUBSET = "s";

    private static final String ALPHA = "a";

    private static final String MESH = "m";

    private CollectionManager collectionManager;

    protected String query;

    protected String subset;

    protected String type;

    protected String alpha;

    protected String mesh;

    private String mode;

    private XMLConsumer xmlConsumer;

    public void setCollectionManager(final CollectionManager collectionManager) {
        if (null == collectionManager) {
            throw new IllegalArgumentException("null collectionManager");
        }
        this.collectionManager = collectionManager;
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        this.mode = par.getParameter("mode", "browse");
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.query = request.getParameter(QUERY);
        if (null != this.query) {
            if (this.query.length() == 0) {
                this.query = null;
            }
        }
        this.type = request.getParameter(TYPE);
        if (null != this.type) {
            if (this.type.length() == 0) {
                this.type = null;
            }
        }
        this.subset = request.getParameter(SUBSET);
        if (null != this.subset) {
            if (this.subset.length() == 0) {
                this.subset = null;
            }
        }
        this.alpha = request.getParameter(ALPHA);
        if (this.alpha != null) {
            if (this.alpha.length() == 0) {
                this.alpha = null;
            }
        }
        this.mesh = request.getParameter(MESH);
        if (this.mesh != null) {
            if (this.mesh.length() == 0) {
                this.mesh = null;
            } else {
                this.mesh = this.mesh.toLowerCase();
            }
        }
    }

    public void generate() throws SAXException {
        XMLizable eresources = new XHTMLizableEresourceList(getEresourceList());
        this.xmlConsumer.startDocument();
        eresources.toSAX(this.xmlConsumer);
        this.xmlConsumer.endDocument();
    }

    protected Collection<Eresource> getEresourceList() {
        if ("search".equals(this.mode)) {
            if (null == this.query) {
                throw new IllegalStateException("null query");
            }
            if (null != this.type) {
                return this.collectionManager.searchType(this.type, this.query);
            } else if (null != this.subset) {
                return this.collectionManager.searchSubset(this.subset, this.query);
            }
            return this.collectionManager.search(this.query);
        } else if ("browse".equals(this.mode)) {
            if (null != this.type) {
                if (null != this.alpha) {
                    return this.collectionManager.getType(this.type, this.alpha.charAt(0));
                }
                return this.collectionManager.getType(this.type);
            } else if (null != this.subset) {
                return this.collectionManager.getSubset(this.subset);
            } else {
                throw new IllegalStateException("null type or subset");
            }
        } else if ("core".equals(this.mode)) {
            if (null == this.type) {
                throw new IllegalStateException("null type");
            } else if (null != this.mesh) {
                return this.collectionManager.getMeshCore(this.type, this.mesh);
            }
            return this.collectionManager.getCore(this.type);
        } else if ("mesh".equals(this.mode)) {
            if (null == this.type) {
                throw new IllegalStateException("null type");
            }
            return this.collectionManager.getMesh(this.type, this.mesh);
        }
        throw new IllegalStateException("incomplete parameters");
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        if (null == xmlConsumer) {
            throw new IllegalArgumentException("null xmlConsumer");
        }
        this.xmlConsumer = xmlConsumer;
    }

}
