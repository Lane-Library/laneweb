package edu.stanford.irt.laneweb.metasearch;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.search.impl.Result;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private MetaSearchService metaSearchService;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() throws Exception {
        this.metaSearchService = mock(MetaSearchService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new DescribeGenerator(this.metaSearchService, this.saxStrategy);
        this.result = mock(Result.class);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.metaSearchService.describe("", null)).andReturn(this.result);
        this.saxStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.metaSearchService, this.xmlConsumer, this.saxStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.metaSearchService, this.xmlConsumer, this.saxStrategy);
    }

    @Test
    public void testGetKey() {
        assertEquals("describe", this.generator.getKey());
    }

    @Test
    public void testGeType() {
        assertEquals("describe", this.generator.getType());
    }

    @Test
    public void testValid() {
        assertTrue(this.generator.getValidity().isValid());
    }
}
