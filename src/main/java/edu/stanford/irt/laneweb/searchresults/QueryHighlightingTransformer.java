package edu.stanford.irt.laneweb.searchresults;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

public class QueryHighlightingTransformer extends AbstractTransformer {
    
    private static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_ ]");

    private static final String INVERT_REPLACEMENT = "$1 and ($4 $2)";

    private static final String PERIOD = "\\.";

    public static final String EMPTY = "";

    private static final Pattern HYPHEN_PATTERN = Pattern.compile("\\-");
    

    private Pattern queryPattern;

    private CharBuffer chars;

    @Override
    protected void initialize() {
        String query = this.model.getString(LanewebObjectModel.QUERY);
        if (null == query) {
            throw new IllegalArgumentException("null query");
        }
        String normalQuery;
        normalQuery = query.toLowerCase();
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = HYPHEN_PATTERN.matcher(normalQuery).replaceAll(PERIOD);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(EMPTY);
        normalQuery = normalQuery.replaceAll(" and ", "|");
        this.queryPattern = Pattern.compile(normalQuery);
        this.chars = CharBuffer.allocate(256);
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        while (this.chars.remaining() < length) {
            CharBuffer newChars = CharBuffer.allocate(this.chars.capacity() + 256);
            int position = this.chars.position();
            this.chars.rewind();
            newChars.append(this.chars.subSequence(0, position));
            this.chars = newChars;
        }
        this.chars.put(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (this.chars.position() != 0) {
            handleMatches();
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes atts) throws SAXException {
        if (this.chars.position() != 0) {
            handleMatches();
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }

    private void handleMatches() throws SAXException {
        int position = chars.position();
        this.chars.rewind();
        Matcher matcher = queryPattern.matcher(this.chars);
            int currentInd = 0;
            while (matcher.find()) {
                int start = matcher.start();
                int end = matcher.end();
                this.xmlConsumer.characters(this.chars.array(), currentInd, start);
                currentInd = end;
                this.xmlConsumer.startElement(SearchResultHelper.NAMESPACE, SearchResultHelper.KEYWORD, SearchResultHelper.KEYWORD, EMPTY_ATTRIBUTES);
                char[] match = matcher.group().toCharArray();
                this.xmlConsumer.characters(match, 0, match.length);
                this.xmlConsumer.endElement(SearchResultHelper.NAMESPACE, SearchResultHelper.KEYWORD, SearchResultHelper.KEYWORD);
            }
        if (currentInd != position) {
            this.xmlConsumer.characters(this.chars.array(), currentInd, position - currentInd);
        }
        this.chars.clear();
    }
}
