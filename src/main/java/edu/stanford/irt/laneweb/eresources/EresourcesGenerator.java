package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.avalon.framework.service.ServiceException;
import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.ServiceableGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.eresources.CollectionManager;
import edu.stanford.irt.eresources.Eresource;

public class EresourcesGenerator extends ServiceableGenerator {

    private static final String KEYWORDS = "keywords";

    private static final String QUERY = "q";

    private static final String TYPE = "t";

    private static final String SUBSET = "s";

    private static final String ALPHA = "a";

    private static final String MESH = "m";

    private static final Attributes EMPTY_ATTS = new AttributesImpl();
    
    private CollectionManager collectionManager;

    protected String query;

    protected String subset;

    protected String type;

    protected String alpha;

    protected String mesh;
    
    private String mode;

    @Override
    public void setup(final SourceResolver resolver, final Map objectModel,
            final String src, final Parameters par) throws ProcessingException,
            SAXException, IOException {
        super.setup(resolver, objectModel, src, par);
        this.mode = par.getParameter("mode", "browse");
        Request request = ObjectModelHelper.getRequest(objectModel);
        this.query = request.getParameter(QUERY);
        if (null == this.query) {
            this.query = request.getParameter(KEYWORDS);
        }
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

    public void generate() throws SAXException, ProcessingException {
        Collection<Eresource> eresources = getEresourceList();
        String title = "edu.stanford.irt.eresources.Eresource";
        EresourceSAXTranslator translator = new EresourceSAXTranslator();
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "html",
                "html", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "head",
                "head", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "title",
                "title", EMPTY_ATTS);
        this.xmlConsumer.characters(title.toCharArray(), 0, title.length());
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "title",
                "title");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "head",
                "head");
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "body",
                "body", EMPTY_ATTS);
        this.xmlConsumer.startElement("http://www.w3.org/1999/xhtml", "dl",
                "dl", EMPTY_ATTS);
        for (Eresource er : eresources) {
            translator.translate(this.xmlConsumer, er);
        }
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "dl", "dl");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "body",
                "body");
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "html",
                "html");
        this.xmlConsumer.endPrefixMapping("");
        this.xmlConsumer.endDocument();
    }

    @Override
    public void service(final ServiceManager manager) throws ServiceException {
        super.service(manager);
        this.collectionManager = (CollectionManager) manager.lookup(CollectionManager.class.getName());
    }

    @Override
    public void dispose() {
        this.manager.release(this.collectionManager);
        super.dispose();
    }
    
    @Override
    public void recycle() {
        this.mode = null;
        this.alpha = null;
        this.mesh = null;
        this.query = null;
        this.type = null;
        this.subset = null;
    }

    protected Collection<Eresource> getEresourceList() throws ProcessingException {
        if ("search".equals(this.mode)) {
            if (null == this.query) {
                throw new ProcessingException("null query");
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
                throw new ProcessingException("null type or subset");
            }
        } else if ("core".equals(this.mode)) {
            if (null == this.type) {
                throw new ProcessingException("null type");
            }
            return this.collectionManager.getCore(this.type);
        } else if ("mesh".equals(this.mode)) {
            if (null == this.type) {
                throw new ProcessingException("null type");
            }
            return this.collectionManager.getMesh(this.type, this.mesh);
        }
        throw new ProcessingException("incomplete parameters");
    }

}
