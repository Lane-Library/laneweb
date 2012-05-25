package edu.stanford.irt.cocoon.pipeline.transform;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import org.apache.cocoon.xml.XMLConsumer;
import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.transform.NamespaceFilter;

public class NamespaceFilterTest {

    private NamespaceFilter filter;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() throws Exception {
        this.filter = new NamespaceFilter();
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.filter.setConsumer(this.xmlConsumer);
    }

    @Test
    public void testStartEndPrefixMapping() throws SAXException {
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.endPrefixMapping("");
        replay(this.xmlConsumer);
        this.filter.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.filter.endPrefixMapping("");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartEndPrefixMappingOther() throws SAXException {
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.endPrefixMapping("");
        replay(this.xmlConsumer);
        this.filter.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.filter.startPrefixMapping("xi", "http://www.w3.org/2001/XInclude");
        this.filter.endPrefixMapping("xi");
        this.filter.endPrefixMapping("");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartEndPrefixMappingOtherEmpty() throws SAXException {
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.endPrefixMapping("");
        replay(this.xmlConsumer);
        this.filter.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.filter.startPrefixMapping("", "http://www.w3.org/2001/XInclude");
        this.filter.endPrefixMapping("");
        this.filter.endPrefixMapping("");
        verify(this.xmlConsumer);
    }

    @Test
    public void testStartEndPrefixMappingTwice() throws SAXException {
        this.xmlConsumer.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.xmlConsumer.endPrefixMapping("");
        replay(this.xmlConsumer);
        this.filter.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.filter.startPrefixMapping("", "http://www.w3.org/1999/xhtml");
        this.filter.endPrefixMapping("");
        this.filter.endPrefixMapping("");
        verify(this.xmlConsumer);
    }
}
