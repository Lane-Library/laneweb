package edu.stanford.irt.cocoon.sitemap.expression;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.service.ServiceManager;
import org.apache.cocoon.components.treeprocessor.InvokeContext;
import org.apache.cocoon.el.objectmodel.ObjectModel;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.CocoonException;

public class LanewebVariableResolverTest {

    private InvokeContext context;

    private Expression expression;

    private ServiceManager manager;

    private Map<String, Object> model;

    private ObjectModel objectModel;

    private ExpressionParser parser;

    private VariableImpl resolver;

    @Before
    public void setUp() throws Exception {
        this.parser = createMock(ExpressionParser.class);
        this.resolver = new VariableImpl(this.parser);
        this.context = new InvokeContext();
        this.model = new HashMap<String, Object>();
        this.manager = createMock(ServiceManager.class);
        this.objectModel = createMock(ObjectModel.class);
        expect(this.manager.lookup("org.apache.cocoon.el.objectmodel.ObjectModel")).andReturn(this.objectModel);
        replay(this.manager);
        this.context.service(this.manager);
        verify(this.manager);
        this.expression = createMock(Expression.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testResolveInvokeContextMap() throws IOException {
        this.context.pushMap(null, Collections.singletonMap("0", "foo"));
        this.context.pushMap(null, Collections.singletonMap("0", "bar"));
        expect(this.parser.parseExpression("{../0}{0}")).andReturn(
                Arrays.asList(new Expression[] { this.expression, this.expression }));
        expect(this.expression.getValue(isA(Map.class))).andReturn("foo");
        expect(this.expression.getValue(isA(Map.class))).andReturn("bar");
        replay(this.parser, this.expression);
        this.resolver.setExpression("{../0}{0}");
        assertEquals("foobar", this.resolver.resolve(this.context, this.model));
        verify(this.parser, this.expression);
    }

    @Test
    public void testSetExpression() throws IOException {
        expect(this.parser.parseExpression("foo")).andReturn(Collections.<Expression> singletonList(this.expression));
        replay(this.parser, this.expression);
        this.resolver.setExpression("foo");
        verify(this.parser, this.expression);
    }

    @Test
    public void testSetExpressionThrow() throws IOException {
        expect(this.parser.parseExpression("foo")).andThrow(new IOException());
        replay(this.parser, this.expression);
        try {
            this.resolver.setExpression("foo");
            fail();
        } catch (CocoonException e) {
        }
        verify(this.parser, this.expression);
    }
}
