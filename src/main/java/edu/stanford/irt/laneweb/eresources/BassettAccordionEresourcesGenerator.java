package edu.stanford.irt.laneweb.eresources;

import java.io.IOException;
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

public class BassettAccordionEresourcesGenerator implements Generator {

    private static final String QUERY = "q";

    private BassettCollectionManager collectionManager;
    private XMLConsumer xmlConsumer;

    private String query;

    public void setCollectionManager(final BassettCollectionManager collectionManager) {
	if (null == collectionManager) {
	    throw new IllegalArgumentException("null collectionManager");
	}
	this.collectionManager = collectionManager;
    }

    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
	    throws ProcessingException, SAXException, IOException {
	Request request = ObjectModelHelper.getRequest(objectModel);
	String query = request.getParameter(QUERY);
	if (null != query) {
	    this.query = query.trim();
	    if (this.query.length() == 0) {
		this.query = null;
	    }
	}
    }

    public void generate() throws SAXException {
	if (this.query == null)
	    this.query = "bassett";
	Map<String, Integer> regionCountMap = this.collectionManager.searchCount(null, null, this.query);
	this.xmlConsumer.startDocument();
	XMLizable xml = new XMLLizableBassettCount(regionCountMap);
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
