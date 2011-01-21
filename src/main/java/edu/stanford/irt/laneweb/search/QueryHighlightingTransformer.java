package edu.stanford.irt.laneweb.search;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;
import edu.stanford.irt.laneweb.resource.Resource;

public class QueryHighlightingTransformer extends AbstractTransformer {

    public static final String EMPTY = "";

    private static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    private CharBuffer chars;

    private int parseLevel = 0;

    private Pattern queryPattern;

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (this.parseLevel > 0) {
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
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if ("title".equals(localName) || "description".equals(localName)) {
            ++this.parseLevel;
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

    @Override
    protected void initialize() {
        String query = ModelUtil.getString(this.model, Model.QUERY);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.queryPattern = QueryTermPattern.getPattern(query);
        this.chars = CharBuffer.allocate(256);
    }
}
