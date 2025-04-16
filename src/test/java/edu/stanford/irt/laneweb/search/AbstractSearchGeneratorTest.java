package edu.stanford.irt.laneweb.search;

import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.model.Model;

public class AbstractSearchGeneratorTest {

    private static final class TestAbstractSearchGenerator extends AbstractSearchGenerator<Object> {

        public TestAbstractSearchGenerator(final SAXStrategy<Object> saxStrategy) {
            super(saxStrategy);
        }

        @Override
        protected Object doSearch(final String query) {
            return null;
        }

        @Override
        protected Object getEmptyResult() {
            return EMPTY_RESULT;
        }
    }

    static final Object EMPTY_RESULT = new Object();;

    private AbstractSearchGenerator<Object> generator;

    private SAXStrategy<Object> saxStrategy;

    @BeforeEach
    public void setUp() throws Exception {
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new TestAbstractSearchGenerator(this.saxStrategy);
    }

    @Test
    public void testDoGenerateEmptyQuery() {
        this.generator.setModel(Collections.singletonMap(Model.QUERY, ""));
        this.saxStrategy.toSAX(EMPTY_RESULT, null);
        replay(this.saxStrategy);
        this.generator.doGenerate(null);
        verify(this.saxStrategy);
    }

    @Test
    public void testDoGenerateNullQuery() {
        this.saxStrategy.toSAX(EMPTY_RESULT, null);
        replay(this.saxStrategy);
        this.generator.doGenerate(null);
        verify(this.saxStrategy);
    }

    @Test
    public void testSetModel() {
        this.generator.setModel(Collections.singletonMap(Model.QUERY, "query"));
        this.saxStrategy.toSAX(null, null);
        replay(this.saxStrategy);
        this.generator.doGenerate(null);
        verify(this.saxStrategy);
    }
}
