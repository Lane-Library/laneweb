package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.irt.search.ContentResult;
import edu.stanford.irt.search.Result;

public class ContentResultXHTMLSAXStrategyTest {
	
	private ContentResultXHTMLSAXStrategy strategy;
	private TestXMLConsumer xmlConsumer;
	private ContentResultSearchResult result;
	private ContentResult contentResult;
	private Result resourceResult;
	
	@Before
	public void setUp() {
		this.strategy = new ContentResultXHTMLSAXStrategy();
		this.xmlConsumer = new TestXMLConsumer();
		this.result = createMock(ContentResultSearchResult.class);
		this.contentResult = createMock(ContentResult.class);
		this.resourceResult = createMock(Result.class);
	}

	@Test
	public void testToSAX() throws SAXException, IOException {
		expect(this.result.getContentResult()).andReturn(this.contentResult);
		expect(this.result.getResourceResult()).andReturn(this.resourceResult);
		expect(this.resourceResult.getDescription()).andReturn("description");
		expect(this.resourceResult.getHits()).andReturn("20");
		expect(this.contentResult.getURL()).andReturn("url");
		expect(this.contentResult.getTitle()).andReturn("title");
		expect(this.contentResult.getAuthor()).andReturn("author");
		expect(this.contentResult.getPublicationTitle()).andReturn(null);
//		expect(this.contentResult.getPublicationDate()).andReturn("date");
//		expect(this.contentResult.getPublicationVolume()).andReturn("volume");
//		expect(this.contentResult.getPublicationIssue()).andReturn("issue");
//		expect(this.contentResult.getPages()).andReturn("pages");
//		expect(this.contentResult.getContentId()).andReturn("id");
		expect(this.resourceResult.getURL()).andReturn("url");
		expect(this.contentResult.getDescription()).andReturn("description");
		replay(this.result, this.contentResult, this.resourceResult);
		
		this.xmlConsumer.startDocument();
		XMLUtils.startElement(this.xmlConsumer, "", "test");
		this.strategy.toSAX(this.result, this.xmlConsumer);
		XMLUtils.endElement(xmlConsumer, "", "test");
		this.xmlConsumer.endDocument();
		assertEquals(this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAX.xml"), this.xmlConsumer.getStringValue());
		verify(this.result, this.contentResult, this.resourceResult);
	}

	@Test
	public void testToSAXPubMed() throws SAXException, IOException {
		expect(this.result.getContentResult()).andReturn(this.contentResult);
		expect(this.result.getResourceResult()).andReturn(this.resourceResult);
		expect(this.resourceResult.getDescription()).andReturn("PubMed");
		expect(this.resourceResult.getHits()).andReturn("20");
		expect(this.contentResult.getURL()).andReturn("url");
		expect(this.contentResult.getTitle()).andReturn("title");
		expect(this.contentResult.getAuthor()).andReturn("author");
		expect(this.contentResult.getPublicationTitle()).andReturn("title");
		expect(this.contentResult.getPublicationDate()).andReturn("date");
		expect(this.contentResult.getPublicationVolume()).andReturn("volume");
		expect(this.contentResult.getPublicationIssue()).andReturn("issue");
//		expect(this.contentResult.getPages()).andReturn("pages");
		expect(this.contentResult.getContentId()).andReturn("PMID:12");
//		expect(this.resourceResult.getURL()).andReturn("url");
		expect(this.contentResult.getDescription()).andReturn("description");
		replay(this.result, this.contentResult, this.resourceResult);
		
		this.xmlConsumer.startDocument();
		XMLUtils.startElement(this.xmlConsumer, "", "test");
		this.strategy.toSAX(this.result, this.xmlConsumer);
		XMLUtils.endElement(xmlConsumer, "", "test");
		this.xmlConsumer.endDocument();
//		System.out.println(this.xmlConsumer.getStringValue());
		assertEquals(this.xmlConsumer.getExpectedResult(this, "ContentResultXHTMLSAXStrategyTest-testToSAXPubMed.xml"), this.xmlConsumer.getStringValue());
		verify(this.result, this.contentResult, this.resourceResult);
	}

}
