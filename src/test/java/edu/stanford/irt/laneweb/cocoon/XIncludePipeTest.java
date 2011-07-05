package edu.stanford.irt.laneweb.cocoon;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.net.MalformedURLException;

import org.apache.cocoon.environment.SourceResolver;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;

public class XIncludePipeTest {
	
	private XIncludePipe pipe;
	
	private SourceResolver sourceResolver;
	
	private Attributes attributes;
	
	@Before
	public void setUp() {
		this.sourceResolver = createMock(SourceResolver.class);
		this.pipe = new XIncludePipe(this.sourceResolver, null, null, null);
		this.attributes = createMock(Attributes.class);
	}

	//This test demonstrated a bug where an exception occurred during include processing but
	//before the try/catch block incrementing the fallback state.
	@Test
	public void testStartXIncludeElement() throws MalformedURLException, IOException, SAXException {
		expect(this.attributes.getValue("http://www.w3.org/XML/1998/namespace", "base")).andReturn(null);
		expect(this.attributes.getValue("", "href")).andReturn("foo");
		expect(this.attributes.getValue("", "parse")).andReturn(null);
		expect(this.attributes.getValue("", "xpointer")).andReturn(null);
		expect(this.sourceResolver.resolveURI("foo")).andThrow(new RuntimeException("oopsie"));
		replay(this.sourceResolver, this.attributes);
		//must call init() to create xmlBaseSupport object
		this.pipe.init(null, null);
		try {
			this.pipe.startElement("http://www.w3.org/2001/XInclude", "include", "xi:include", this.attributes);
		} catch (Exception e) {
			fail("should catch all exceptions");
		}
		verify(this.sourceResolver, this.attributes);
	}

}
