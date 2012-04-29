package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.apache.excalibur.source.SourceValidity;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class LanewebTraxTransformer extends AbstractSitemapModelComponent implements CacheableProcessingComponent, Transformer, SourceAware, ParametersAware, ModelAware {

    private Serializable cacheKey;
    
    private String cacheKeyParameter;

    private Locator locator;

    private Map<String, Object> model;

    private Map<String, String> parameters;

    private Source source;

    private TransformerHandler transformerHandler;
    
    private TraxProcessor traxProcessor;
    
    private SourceValidity validity;

    private XMLConsumer xmlConsumer;

    public LanewebTraxTransformer(final TraxProcessor traxProcessor) {
        this.traxProcessor = traxProcessor;
    }

    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        this.transformerHandler.characters(ch, start, length);
    }

    public void comment(final char[] ch, final int start, final int length) throws SAXException {
        this.transformerHandler.comment(ch, start, length);
    }

    public void endCDATA() throws SAXException {
        this.transformerHandler.endCDATA();
    }

    public void endDocument() throws SAXException {
        this.transformerHandler.endDocument();
    }

    public void endDTD() throws SAXException {
        this.transformerHandler.endDTD();
    }

    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        this.transformerHandler.endElement(uri, localName, qName);
    }

    public void endEntity(final String name) throws SAXException {
        this.transformerHandler.endEntity(name);
    }

    public void endPrefixMapping(final String prefix) throws SAXException {
        this.transformerHandler.endPrefixMapping(prefix);
    }

    public Serializable getKey() {
        if (this.cacheKey == null) {
            this.cacheKey = this.source.getURI();
            if (this.cacheKeyParameter != null) {
                this.cacheKey = this.cacheKey + ";" + this.cacheKeyParameter;
            }
        }
        return this.cacheKey;
    }

    public SourceValidity getValidity() {
        return this.validity;
    }

    public void ignorableWhitespace(final char[] ch, final int start, final int length) throws SAXException {
        this.transformerHandler.ignorableWhitespace(ch, start, length);
    }

    public void notationDecl(final String name, final String publicId, final String systemId) throws SAXException {
        this.transformerHandler.notationDecl(name, publicId, systemId);
    }

    public void processingInstruction(final String target, final String data) throws SAXException {
        this.transformerHandler.processingInstruction(target, data);
    }

    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
    }

    public void setDocumentLocator(final Locator locator) {
        this.locator = locator;
    }

    public void setModel(Map<String, Object> model) {
        this.model = model;
    }

    public void setParameters(Map<String, String> parameters) {
        this.cacheKeyParameter = parameters.get("cache-key");
        this.parameters = parameters;
    }

    public void setSource(Source source) {
        this.source = source;
        this.validity = source.getValidity();
    }

    public void skippedEntity(final String name) throws SAXException {
        this.transformerHandler.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.transformerHandler.startCDATA();
    }

    public void startDocument() throws SAXException {
        setupTransformerHandler();
        if (this.locator != null) {
            this.transformerHandler.setDocumentLocator(this.locator);
        }
        this.transformerHandler.startDocument();
    }

    public void startDTD(final String name, final String publicId, final String systemId) throws SAXException {
        this.transformerHandler.startDTD(name, publicId, systemId);
    }

    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        this.transformerHandler.startElement(uri, localName, qName, atts);
    }

    public void startEntity(final String name) throws SAXException {
        this.transformerHandler.startEntity(name);
    }

    public void startPrefixMapping(final String prefix, final String uri) throws SAXException {
        this.transformerHandler.startPrefixMapping(prefix, uri);
    }

    public void unparsedEntityDecl(final String name, final String publicId, final String systemId, final String notationName)
            throws SAXException {
        this.transformerHandler.unparsedEntityDecl(name, publicId, systemId, notationName);
    }
    
    private void setupTransformerHandler() {
        this.transformerHandler = this.traxProcessor.getTransformerHandler(this.source);
        javax.xml.transform.Transformer transformer = this.transformerHandler.getTransformer();
        for (Entry<String, Object> entry : this.model.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue().toString());
        }
        for (Entry<String, String> entry : this.parameters.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }
        SAXResult result = new SAXResult(this.xmlConsumer);
        result.setLexicalHandler(this.xmlConsumer);
        this.transformerHandler.setResult(result);
    }
}
