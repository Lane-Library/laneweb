package edu.stanford.irt.laneweb.search;

import java.nio.CharBuffer;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.transform.AbstractTransformer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.Resource;

public class QueryHighlightingTransformer extends AbstractTransformer implements ModelAware {

    public static final String EMPTY = "";

    private static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    private CharBuffer chars = CharBuffer.allocate(256);

    private boolean inTargetElement = false;

    private int parseLevel = 0;

    private Pattern queryPattern;

    private XMLConsumer xmlConsumer;

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.parseLevel > 0 && this.queryPattern != null) {
            while (this.chars.remaining() < length) {
                CharBuffer newChars = CharBuffer.allocate(this.chars.capacity() + 256);
                int position = this.chars.position();
                this.chars.rewind();
                newChars.append(this.chars.subSequence(0, position));
                this.chars = newChars;
            }
            this.chars.put(ch, start, length);
        } else {
            this.xmlConsumer.characters(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.parseLevel > 0) {
            handleMatches();
        }
        if ("title".equals(localName) || "description".equals(localName)) {
            this.parseLevel--;
            this.inTargetElement = false;
        }
        // end of child element of title or description
        else if (this.inTargetElement == true) {
            ++this.parseLevel;
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public void setConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setConsumer(xmlConsumer);
    }

    public void setModel(final Map<String, Object> model) {
        String query = ModelUtil.getString(model, Model.QUERY);
        if (query != null) {
            this.queryPattern = QueryTermPattern.getPattern(query);
        }
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (this.queryPattern != null) {
            if ("title".equals(localName) || "description".equals(localName)) {
                ++this.parseLevel;
                this.inTargetElement = true;
            }
            // don't process child elements of title or description
            else if (this.inTargetElement == true) {
                handleMatches();
                this.parseLevel--;
            }
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }

    private void handleMatches() throws SAXException {
        int charsEnd = this.chars.position();
        this.chars.rewind();
        Matcher matcher = this.queryPattern.matcher(this.chars.subSequence(0, charsEnd));
        int current = 0;
        while (current < charsEnd && matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            if (matchStart > current) {
                // send chars before match:
                this.xmlConsumer.characters(this.chars.array(), current, matchStart - current);
            }
            this.xmlConsumer.startElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD, EMPTY_ATTRIBUTES);
            char[] match = matcher.group().toCharArray();
            this.xmlConsumer.characters(match, 0, match.length);
            this.xmlConsumer.endElement(Resource.NAMESPACE, Resource.KEYWORD, Resource.KEYWORD);
            current = matchEnd;
        }
        if (current < charsEnd) {
            // send chars after last match:
            this.xmlConsumer.characters(this.chars.array(), current, charsEnd - current);
        }
        this.chars.clear();
    }
}
