package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractSearchGeneratorTest {

    private static final class TestAbstractSearchGenerator extends AbstractSearchGenerator<Object> {

        public TestAbstractSearchGenerator(final SAXStrategy<Object> saxStrategy) {
            super(saxStrategy);
        }

        protected Object doSearch(final String query) {
            return null;
        }
    };

    private AbstractSearchGenerator<Object> generator;

    private SAXStrategy<Object> saxStrategy;

    @Before
    public void setUp() throws Exception {
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new TestAbstractSearchGenerator(this.saxStrategy);
    }

    @Test
    public void testDoGenerate() {
        this.saxStrategy.toSAX(null, null);
        replay(this.saxStrategy);
        this.generator.doGenerate(null);
        verify(this.saxStrategy);
    }
    
    @Test
    public void testSetModel() {
        this.generator.setModel(Collections.<String, Object>singletonMap(Model.QUERY, "query"));
    }
}
