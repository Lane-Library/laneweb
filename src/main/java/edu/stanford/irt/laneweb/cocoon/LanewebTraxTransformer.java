package edu.stanford.irt.laneweb.cocoon;

import java.io.Serializable;
import java.util.Map.Entry;

import javax.xml.transform.Result;
import javax.xml.transform.sax.SAXResult;
import javax.xml.transform.sax.TransformerHandler;

import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.transformation.Transformer;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.xml.xslt.XSLTProcessor;
import org.apache.excalibur.xml.xslt.XSLTProcessorException;
import org.xml.sax.Attributes;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;

public class LanewebTraxTransformer extends AbstractSitemapModelComponent implements CacheableProcessingComponent, Transformer {

    private Serializable cacheKey;

    private TransformerHandler transformerHandler;

    private XSLTProcessor xsltProcessor;

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
        return this.cacheKey;
    }

    public String getSystemId() {
        return this.transformerHandler.getSystemId();
    }

    public SourceValidity getValidity() {
        return this.source.getValidity();
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
        if (this.transformerHandler == null) {
            try {
                this.transformerHandler = this.xsltProcessor.getTransformerHandler(this.source);
            } catch (XSLTProcessorException se) {
                throw new IllegalStateException(se);
            }
        }
        javax.xml.transform.Transformer transformer = this.transformerHandler.getTransformer();
        for (Entry<String, Object> entry : this.model.entrySet()) {
            if (entry.getValue() == null) {
                throw new IllegalStateException("null value in model for " + entry.getKey());
            }
            transformer.setParameter(entry.getKey(), entry.getValue().toString());
        }
        for (Entry<String, String> entry : this.parameterMap.entrySet()) {
            transformer.setParameter(entry.getKey(), entry.getValue());
        }
        final SAXResult result = new SAXResult(xmlConsumer);
        result.setLexicalHandler(xmlConsumer);
        this.transformerHandler.setResult(result);
    }

    public void setDocumentLocator(final Locator locator) {
        this.transformerHandler.setDocumentLocator(locator);
    }

    public void setResult(final Result result) throws IllegalArgumentException {
        this.transformerHandler.setResult(result);
    }

    public void setSystemId(final String systemID) {
        this.transformerHandler.setSystemId(systemID);
    }

    public void setXsltProcessor(final XSLTProcessor xsltProcessor) {
        this.xsltProcessor = xsltProcessor;
    }

    public void skippedEntity(final String name) throws SAXException {
        this.transformerHandler.skippedEntity(name);
    }

    public void startCDATA() throws SAXException {
        this.transformerHandler.startCDATA();
    }

    public void startDocument() throws SAXException {
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

    @Override
    protected void initialize() {
        this.cacheKey = this.source.getURI();
        if (this.parameterMap.containsKey("cache-key")) {
            this.cacheKey = this.cacheKey + ";" + this.parameterMap.get("cache-key");
        }
    }
}
