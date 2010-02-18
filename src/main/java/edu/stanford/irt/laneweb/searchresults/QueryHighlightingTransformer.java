package edu.stanford.irt.laneweb.searchresults;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

// $Id$
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
        int position = this.chars.position();
        this.chars.rewind();
        Matcher matcher = this.queryPattern.matcher(this.chars);
        int currentInd = 0;
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            this.xmlConsumer.characters(this.chars.array(), currentInd, start - currentInd);
            currentInd = end;
            this.xmlConsumer.startElement(SearchResultHelper.NAMESPACE, SearchResultHelper.KEYWORD,
                    SearchResultHelper.KEYWORD, EMPTY_ATTRIBUTES);
            char[] match = matcher.group().toCharArray();
            this.xmlConsumer.characters(match, 0, match.length);
            this.xmlConsumer.endElement(SearchResultHelper.NAMESPACE, SearchResultHelper.KEYWORD,
                    SearchResultHelper.KEYWORD);
        }
        if (currentInd < position) {
            this.xmlConsumer.characters(this.chars.array(), currentInd, position - currentInd);
        }
        this.chars.clear();
    }

    @Override
    protected void initialize() {
        String query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        this.queryPattern = Pattern.compile(SearchResultHelper.regexifyQuery(query), Pattern.CASE_INSENSITIVE);
        this.chars = CharBuffer.allocate(256);
    }
}
