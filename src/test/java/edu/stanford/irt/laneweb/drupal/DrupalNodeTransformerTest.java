package edu.stanford.irt.laneweb.drupal;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class DrupalNodeTransformerTest {

    private DrupalAPIService apiService;

    private DrupalNodeTransformer transformer;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.apiService = mock(DrupalAPIService.class);
        this.transformer = new DrupalNodeTransformer(this.apiService);
        this.xmlConsumer = mock(XMLConsumer.class);
        this.transformer.setXMLConsumer(this.xmlConsumer);
    }

    @Test
    public void testProcessingInstruction() throws SAXException {
        this.xmlConsumer.processingInstruction("foo", "bar");
        replay(this.apiService, this.xmlConsumer);
        this.transformer.processingInstruction("foo", "bar");
        verify(this.apiService, this.xmlConsumer);
    }

    @Test
    public void testProcessingInstructionDrupal() throws SAXException {
        expect(this.apiService.getNodeContent("bar")).andReturn("foo");
        this.xmlConsumer.startElement(eq("http://www.w3.org/1999/xhtml"), eq("drupal"), eq("drupal"),
                isA(Attributes.class));
        this.xmlConsumer.characters("foo".toCharArray(), 0, 3);
        this.xmlConsumer.endElement("http://www.w3.org/1999/xhtml", "drupal", "drupal");
        replay(this.apiService, this.xmlConsumer);
        this.transformer.processingInstruction("drupal", "bar");
        verify(this.apiService, this.xmlConsumer);
    }
}
