package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.aryEq;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class AbstractTextProcessingTransformerTest {

    private static class TestAbstractTextProcessingTransformer extends AbstractTextProcessingTransformer {

        private boolean isTargetName;

        private Matcher matcher;

        private Pattern pattern;

        public TestAbstractTextProcessingTransformer(final boolean isTargetName, final Pattern pattern) {
            this.isTargetName = isTargetName;
            this.pattern = pattern;
        }

        public Matcher getMatcher() {
            return this.matcher;
        }

        @Override
        protected void createSAXEvents(final XMLConsumer consumer, final Matcher matcher) throws SAXException {
            this.matcher = matcher;
        }

        @Override
        protected Pattern getPattern() {
            return this.pattern;
        }

        @Override
        protected boolean isTargetName(final String name) {
            return this.isTargetName;
        }
    }

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setup() {
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testLongishText() throws SAXException {
        char[] chars = new char[1000];
        Arrays.fill(chars, 'a');
        TestAbstractTextProcessingTransformer transformer = new TestAbstractTextProcessingTransformer(true,
                Pattern.compile("a*"));
        transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlConsumer.startElement("", "", "", null);
        this.xmlConsumer.endElement("", "", "");
        replay(this.xmlConsumer);
        transformer.startElement("", "", "", null);
        transformer.characters(chars, 0, 1000);
        transformer.endElement("", "", "");
        assertNotNull(transformer.getMatcher());
        verify(this.xmlConsumer);
    }

    @Test
    public void testNestedNotTarget() throws SAXException {
        char[] chars = new char[1000];
        Arrays.fill(chars, 'a');
        TestAbstractTextProcessingTransformer transformer = new TestAbstractTextProcessingTransformer(true,
                Pattern.compile("a*")) {

            @Override
            protected boolean isTargetName(final String name) {
                return "target".equals(name);
            }
        };
        transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlConsumer.startElement("", "nottarget", "nottarget", null);
        this.xmlConsumer.characters(aryEq(chars), eq(0), eq(1000));
        this.xmlConsumer.endElement("", "nottarget", "nottarget");
        this.xmlConsumer.startElement("", "target", "target", null);
        this.xmlConsumer.startElement("", "nottarget", "nottarget", null);
        this.xmlConsumer.characters(aryEq(chars), eq(0), eq(1000));
        this.xmlConsumer.endElement("", "nottarget", "nottarget");
        this.xmlConsumer.endElement("", "target", "target");
        replay(this.xmlConsumer);
        transformer.startElement("", "nottarget", "nottarget", null);
        transformer.characters(chars, 0, 1000);
        transformer.endElement("", "nottarget", "nottarget");
        transformer.startElement("", "target", "target", null);
        transformer.characters(chars, 0, 1000);
        transformer.startElement("", "nottarget", "nottarget", null);
        transformer.characters(chars, 0, 1000);
        transformer.endElement("", "nottarget", "nottarget");
        transformer.endElement("", "target", "target");
        assertNotNull(transformer.getMatcher());
        verify(this.xmlConsumer);
    }

    @Test
    public void testNullPattern() throws SAXException {
        char[] chars = new char[1000];
        Arrays.fill(chars, 'a');
        TestAbstractTextProcessingTransformer transformer = new TestAbstractTextProcessingTransformer(true, null);
        transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlConsumer.startElement("", "", "", null);
        this.xmlConsumer.characters(aryEq(chars), eq(0), eq(1000));
        this.xmlConsumer.endElement("", "", "");
        replay(this.xmlConsumer);
        transformer.startElement("", "", "", null);
        transformer.characters(chars, 0, 1000);
        transformer.endElement("", "", "");
        assertNull(transformer.getMatcher());
        verify(this.xmlConsumer);
    }

    @Test
    public void testTextBeforeAfterMatch() throws SAXException {
        char[] chars = "some characters with ::DESCRIPTION LABEL-DASH/SLASH## inside of it".toCharArray();
        TestAbstractTextProcessingTransformer transformer = new TestAbstractTextProcessingTransformer(true,
                Pattern.compile("::([A-Z '/,-]+)##"));
        transformer.setXMLConsumer(this.xmlConsumer);
        this.xmlConsumer.startElement("", "", "", null);
        this.xmlConsumer.characters(isA(char[].class), eq(0), eq(21));
        this.xmlConsumer.characters(isA(char[].class), eq(53), eq(13));
        this.xmlConsumer.endElement("", "", "");
        replay(this.xmlConsumer);
        transformer.startElement("", "", "", null);
        transformer.characters(chars, 0, chars.length);
        transformer.endElement("", "", "");
        assertNotNull(transformer.getMatcher());
        verify(this.xmlConsumer);
    }
}
