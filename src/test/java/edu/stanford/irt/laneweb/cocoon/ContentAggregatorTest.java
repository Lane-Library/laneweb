package edu.stanford.irt.laneweb.cocoon;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.Map;

import org.apache.cocoon.core.xml.SAXParser;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.xml.XMLConsumer;
import org.apache.excalibur.source.Source;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public class ContentAggregatorTest {
	
	private ContentAggregator aggregator;
	private SAXParser saxParser;
	private XMLConsumer xmlConsumer;
	private SourceResolver sourceResolver;
	private Map model;
	private Source source;
	private InputStream inputStream;

	@Before
	public void setUp() throws Exception {
		this.saxParser = createMock(SAXParser.class);
		this.xmlConsumer = createMock(XMLConsumer.class);
		this.sourceResolver = createMock(SourceResolver.class);
		this.model = Collections.emptyMap();
		this.source = createMock(Source.class);
		this.inputStream = createMock(InputStream.class);
		this.aggregator = new ContentAggregator(this.saxParser);
		this.aggregator.setRootElement("root", "namespace", "prefix");
		this.aggregator.addPart("uri", "", null, null, null);
		this.aggregator.addPart("uri", "part", "", null, null);
		expect(this.sourceResolver.resolveURI("uri")).andReturn(this.source).times(2);
		replay(this.sourceResolver);
		this.aggregator.setup(this.sourceResolver, this.model, null, null);
		verify(this.sourceResolver);
		this.aggregator.setConsumer(this.xmlConsumer);
	}

	@Test
	public void testGenerate() throws IOException, SAXException {
		this.xmlConsumer.startDocument();
		this.xmlConsumer.startPrefixMapping("prefix", "namespace");
		expectLastCall().times(2);
		this.xmlConsumer.startElement(eq("namespace"), eq("root"), eq("prefix:root"), isA(Attributes.class));
		this.xmlConsumer.startElement(eq("namespace"), eq("part"), eq("prefix:part"), isA(Attributes.class));
		expect(this.source.getInputStream()).andReturn(this.inputStream).times(2);
		this.saxParser.parse(isA(InputSource.class), eq(this.aggregator), eq(this.aggregator));
		expectLastCall().times(2);
		this.inputStream.close();
		expectLastCall().times(2);
		this.xmlConsumer.endElement("namespace", "part", "prefix:part");
		this.xmlConsumer.endElement("namespace", "root", "prefix:root");
		this.xmlConsumer.endPrefixMapping("prefix");
		expectLastCall().times(2);
		this.xmlConsumer.endDocument();
		replay(this.saxParser, this.xmlConsumer, this.source, this.inputStream);
		this.aggregator.generate();
		verify(this.saxParser, this.xmlConsumer, this.source, this.inputStream);
	}

}
