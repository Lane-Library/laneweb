package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class TextNodeParsingTransformerTest {
	
	private TextNodeParsingTransformer transformer;
	private Attributes attributes;
	private Parameters parameters;
	private XMLConsumer xmlConsumer;

	@Before
	public void setUp() throws Exception {
		this.transformer = new TextNodeParsingTransformer();
		this.parameters = createMock(Parameters.class);
		expect(this.parameters.getParameter("elementName", null)).andReturn("element");
		replay(this.parameters);
		this.transformer.setup(null, null, null, this.parameters);
		verify(this.parameters);
		this.xmlConsumer = createMock(XMLConsumer.class);
		this.transformer.setConsumer(this.xmlConsumer);
	}

	@Test
	public void testCharacters() throws SAXException {
		this.xmlConsumer.characters(null, 0, 0);
		replay(this.xmlConsumer);
		this.transformer.characters(null, 0, 0);
		verify(this.xmlConsumer);
	}

	@Test
	public void testEndElement() throws SAXException {
		this.xmlConsumer.endElement("uri", "localName", "qName");
		replay(this.xmlConsumer);
		this.transformer.endElement("uri", "localName", "qName");
		verify(this.xmlConsumer);
	}

	@Test
	public void testStartElement() throws SAXException {
		this.xmlConsumer.startElement("uri", "localName", "qName", this.attributes);
		replay(this.xmlConsumer);
		this.transformer.startElement("uri", "localName", "qName", this.attributes);
		verify(this.xmlConsumer);
	}

	@Test
	public void testGetKey() {
		assertEquals("textNodeParsing", this.transformer.getKey());
	}

	@Test
	public void testGetValidity() {
		assertEquals(NOPValidity.SHARED_INSTANCE, this.transformer.getValidity());
	}

}
