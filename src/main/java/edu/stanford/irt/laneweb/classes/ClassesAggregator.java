package edu.stanford.irt.laneweb.classes;

import java.io.IOException;
import java.util.Map;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.ProcessingException;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.cocoon.ContentAggregator;

public class ClassesAggregator extends ContentAggregator implements CacheableProcessingComponent {

    private String detailClasseUrl = "http://onlineregistrationcenter.com/registerxml.asp?m=257&c=";

    private String src;

    private XPath xpath = null;

    public ClassesAggregator(final SAXParser saxParser) {
        super(saxParser);
        super.rootElement = new Element("classes", "http://lane.stanford.edu/laneclasses", "");
        this.xpath = XPathFactory.newInstance().newXPath();
    }

    @Override
    public java.io.Serializable getKey() {
        return this.src;
    }

    @Override
    public SourceValidity getValidity() {
        return new ClassesValidity();
    }

    @SuppressWarnings("rawtypes")
    @Override
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par)
            throws ProcessingException, SAXException, IOException {
        this.resolver = resolver;
        this.src = src;
        Source source = resolver.resolveURI(src);
        InputSource domContent = new InputSource(source.getInputStream());
        domContent.setSystemId(source.getURI());
        try {
            NodeList nodeList = (NodeList) this.xpath.evaluate("/eventlist/event/eventid", domContent, XPathConstants.NODESET);
            for (int i = 0; i < nodeList.getLength(); i++) {
                Node node = nodeList.item(i);
                String uri = this.detailClasseUrl.concat(node.getTextContent());
                this.addPart(uri, "", "", "false", "");
            }
            for (int i = 0; i < this.parts.size(); i++) {
                final Part current = (Part) this.parts.get(i);
                current.source = resolver.resolveURI(current.uri);
            }
        } catch (XPathExpressionException e) {
            throw new RuntimeException(e);
        }
    }
}
