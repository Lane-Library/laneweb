package edu.stanford.irt.laneweb.search;

import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLConsumer;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

import edu.stanford.irt.laneweb.cocoon.AbstractTransformer;
import edu.stanford.irt.laneweb.resource.Resource;

public class DescriptionLabelTransformer extends AbstractTransformer {

    public static final String EMPTY = "";

    private static final Attributes EMPTY_ATTRIBUTES = new AttributesImpl();

    private static final Pattern LABEL_PATTERN = Pattern.compile("::([A-Z '/,]+)##");

    private CharBuffer chars = CharBuffer.allocate(256);

    private boolean inDescriptionElement = false;

    private int parseLevel = 0;

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
            getXMLConsumer().characters(ch, start, length);
        }
    }

    @Override
    public void endElement(final String uri, final String localName, final String qName) throws SAXException {
        if (this.parseLevel > 0) {
            handleMatches();
        }
        if ("description".equals(localName)) {
            this.parseLevel--;
            this.inDescriptionElement = false;
        }
        // end of child element of description
        else if (this.inDescriptionElement) {
            ++this.parseLevel;
        }
        getXMLConsumer().endElement(uri, localName, qName);
    }

    private void handleMatches() throws SAXException {
        int charsEnd = this.chars.position();
        this.chars.rewind();
        Matcher matcher = LABEL_PATTERN.matcher(this.chars.subSequence(0, charsEnd));
        int current = 0;
        XMLConsumer xmlConsumer = getXMLConsumer();
        while (current < charsEnd && matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            if (matchStart > current) {
                // send chars before match:
                xmlConsumer.characters(this.chars.array(), current, matchStart - current);
            }
            xmlConsumer.startElement(Resource.NAMESPACE, Resource.DESCRIPTION_LABEL, Resource.DESCRIPTION_LABEL,
                    EMPTY_ATTRIBUTES);
            char[] match = matcher.group(1).toCharArray();
            xmlConsumer.characters(match, 0, match.length);
            xmlConsumer.endElement(Resource.NAMESPACE, Resource.DESCRIPTION_LABEL, Resource.DESCRIPTION_LABEL);
            current = matchEnd;
        }
        if (current < charsEnd) {
            // send chars after last match:
            xmlConsumer.characters(this.chars.array(), current, charsEnd - current);
        }
        this.chars.clear();
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if ("description".equals(localName)) {
            ++this.parseLevel;
            this.inDescriptionElement = true;
        }
        // don't process child elements of description
        else if (this.inDescriptionElement) {
            handleMatches();
            this.parseLevel--;
        }
        getXMLConsumer().startElement(uri, localName, qName, atts);
    }
}
