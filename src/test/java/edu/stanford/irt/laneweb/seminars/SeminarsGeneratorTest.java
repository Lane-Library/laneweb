/**
 *
 */
package edu.stanford.irt.laneweb.seminars;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.net.URI;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.source.Source;
import edu.stanford.irt.cocoon.source.SourceResolver;
import edu.stanford.irt.cocoon.xml.EmbeddedXMLPipe;
import edu.stanford.irt.cocoon.xml.SAXParser;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

/**
 * @author ryanmax
 */
public class SeminarsGeneratorTest {

    private SeminarsGenerator generator;

    private SAXParser saxParser;

    private Source source;

    private SourceResolver sourceResolver;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.saxParser = createMock(SAXParser.class);
        this.sourceResolver = createMock(SourceResolver.class);
        this.generator = new SeminarsGenerator(this.saxParser, this.sourceResolver);
        this.source = createMock(Source.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public final void testGenerate() throws SAXException {
        expect(this.sourceResolver.resolveURI(isA(URI.class))).andReturn(this.source);
        this.xmlConsumer.startDocument();
        this.xmlConsumer.startElement(eq("http://lane.stanford.edu/seminars/ns"), eq("seminars"), eq("seminars"),
                isA(Attributes.class));
        this.saxParser.parse(eq(this.source), isA(EmbeddedXMLPipe.class));
        this.xmlConsumer.endElement(eq("http://lane.stanford.edu/seminars/ns"), eq("seminars"), eq("seminars"));
        this.xmlConsumer.endDocument();
        replay(this.saxParser, this.sourceResolver, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.saxParser, this.sourceResolver, this.xmlConsumer);
    }
}
