package edu.stanford.irt.laneweb.search;

import java.io.Serializable;
import java.nio.CharBuffer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.cache.validity.AlwaysValid;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.cocoon.xml.AbstractXMLPipe;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public abstract class AbstractTextProcessingTransformer extends AbstractXMLPipe implements Transformer {

    private static final int BUFFER_SIZE = 256;

    private CharBuffer chars = CharBuffer.allocate(BUFFER_SIZE);

    private boolean inTargetElement;

    private int parseLevel;

    private XMLConsumer xmlConsumer;

    @Override
    public void characters(final char[] ch, final int start, final int length) throws SAXException {
        if (getPattern() != null && this.parseLevel > 0) {
            while (this.chars.remaining() < length) {
                CharBuffer newChars = CharBuffer.allocate(this.chars.capacity() + BUFFER_SIZE);
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
        if (isTargetName(localName)) {
            this.parseLevel--;
            this.inTargetElement = false;
        } else if (this.inTargetElement) {
            // end of child element of target
            ++this.parseLevel;
        }
        this.xmlConsumer.endElement(uri, localName, qName);
    }

    @Override
    public Serializable getKey() {
        return "none";
    }

    @Override
    public Validity getValidity() {
        return AlwaysValid.SHARED_INSTANCE;
    }

    @Override
    public void setXMLConsumer(final XMLConsumer xmlConsumer) {
        this.xmlConsumer = xmlConsumer;
        super.setXMLConsumer(xmlConsumer);
    }

    @Override
    public void startElement(final String uri, final String localName, final String qName, final Attributes atts)
            throws SAXException {
        if (getPattern() != null) {
            if (isTargetName(localName)) {
                ++this.parseLevel;
                this.inTargetElement = true;
            } else if (this.inTargetElement) {
                // don't process child elements of target
                handleMatches();
                this.parseLevel--;
            }
        }
        this.xmlConsumer.startElement(uri, localName, qName, atts);
    }

    protected abstract void createSAXEvents(XMLConsumer consumer, Matcher matcher) throws SAXException;

    protected abstract Pattern getPattern();

    protected abstract boolean isTargetName(final String name);

    private void handleMatches() throws SAXException {
        int charsEnd = this.chars.position();
        this.chars.rewind();
        Matcher matcher = getPattern().matcher(this.chars.subSequence(0, charsEnd));
        int current = 0;
        while (current < charsEnd && matcher.find()) {
            int matchStart = matcher.start();
            int matchEnd = matcher.end();
            if (matchStart > current) {
                // send chars before match:
                this.xmlConsumer.characters(this.chars.array(), current, matchStart - current);
            }
            createSAXEvents(this.xmlConsumer, matcher);
            current = matchEnd;
        }
        if (current < charsEnd) {
            // send chars after last match:
            this.xmlConsumer.characters(this.chars.array(), current, charsEnd - current);
        }
        this.chars.clear();
    }
}
