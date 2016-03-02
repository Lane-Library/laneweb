package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.isNull;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collection;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;

public class DescribeGeneratorTest {

    private DescribeGenerator generator;

    private MetaSearchManager metaSearchManager;

    private Result result;

    private SAXStrategy<Result> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() throws Exception {
        this.metaSearchManager = createMock(MetaSearchManager.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new DescribeGenerator(this.metaSearchManager, this.saxStrategy);
        this.result = createMock(Result.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testDoGenerate() {
        expect(this.metaSearchManager.describe(isA(Query.class), isNull(Collection.class))).andReturn(this.result);
        this.saxStrategy.toSAX(this.result, this.xmlConsumer);
        replay(this.metaSearchManager, this.xmlConsumer, this.saxStrategy);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.metaSearchManager, this.xmlConsumer, this.saxStrategy);
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
