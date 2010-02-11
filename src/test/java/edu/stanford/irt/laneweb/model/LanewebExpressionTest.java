package edu.stanford.irt.laneweb.model;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;

import org.apache.cocoon.el.ExpressionException;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.junit.Before;
import org.junit.Test;

/*
 * $Id$
 */
public class LanewebExpressionTest {

    private LanewebExpression expression;

    private ObjectModel objectModel;

    @Before
    public void setUp() throws Exception {
        this.objectModel = createMock(ObjectModel.class);
        this.expression = new LanewebExpression("bar");
    }

    @Test
    public void testAssign() throws ExpressionException {
        replayMocks();
        try {
            this.expression.assign(this.objectModel, null);
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verifyMocks();
    }

    @Test
    public void testEvaluate() throws ExpressionException {
        expect(this.objectModel.get("laneweb")).andReturn(Collections.singletonMap("bar", "foo"));
        replayMocks();
        assertEquals("foo", this.expression.evaluate(this.objectModel));
        verifyMocks();
    }

    @Test
    public void testGetNode() throws ExpressionException {
        replayMocks();
        try {
            this.expression.getNode(this.objectModel);
            fail();
        } catch (UnsupportedOperationException e) {
        }
        verifyMocks();
    }

    @Test
    public void testIterate() throws ExpressionException {
        replayMocks();
        try {
        this.expression.iterate(this.objectModel);
        fail();
        } catch (UnsupportedOperationException e) {
        }
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.objectModel);
    }

    private void verifyMocks() {
        verify(this.objectModel);
    }
}
