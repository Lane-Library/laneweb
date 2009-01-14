package edu.stanford.irt.laneweb.search;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.generation.Generator;
import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.SAXException;

import edu.stanford.irt.search.MetaSearchManager;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.Result;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.util.SAXResult;
import edu.stanford.irt.search.util.SAXable;

public class DescribeGenerator implements Generator {

    private MetaSearchManager metaSearchManager;

    private String q;

    private String[] e;

    private String admin;

    private String engineId;

    private XMLConsumer xmlConsumer;

    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.metaSearchManager = msms.getMetaSearchManager();
    }

    @SuppressWarnings("unchecked")
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        Request request = (Request) objectModel.get(ObjectModelHelper.REQUEST_OBJECT);
        this.q = request.getParameter("q");
        this.e = request.getParameterValues("e");
        this.admin = request.getParameter("admin");
        this.engineId = request.getParameter("id");
    }

    public void generate() throws SAXException {

        if (this.admin != null) {
            if ("rem".equals(this.admin)) {
                this.metaSearchManager.removeSearchable(this.engineId);
            } else if ("add".equals(this.admin)) {
                this.metaSearchManager.addSearchable(this.engineId);
            }
        }

        Result result = null;

        Collection<String> engines = null;
        if ((this.e != null) && (this.e.length > 0)) {
            engines = new ArrayList<String>();
            for (String element : this.e) {
                engines.add(element);
            }
        }
        Query query = null;
        if (this.q != null) {
            query = new SimpleQuery(this.q);
        } else {
            query = new SimpleQuery("");
        }

        result = this.metaSearchManager.describe(query, engines);

        if (result == null) {
            throw new RuntimeException("result is null, something is broken");
        }

        SAXable xml = new SAXResult(result);
        synchronized (result) {
            xml.toSAX(this.xmlConsumer);
        }
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

}
