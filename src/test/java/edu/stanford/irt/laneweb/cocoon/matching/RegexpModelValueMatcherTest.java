package edu.stanford.irt.laneweb.cocoon.matching;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

public class RegexpModelValueMatcherTest {

    private RegexpModelValueMatcher matcher;

    private Map<String, Object> model;

    private Parameters parameters;

    @Before
    public void setUp() throws Exception {
        this.matcher = new RegexpModelValueMatcher();
        this.model = new HashMap<String, Object>();
        this.parameters = createMock(Parameters.class);
    }

    @Test
    public void testGetMatchStringMapParameters() {
        expect(this.parameters.getParameter("parameter-name", null)).andReturn("bar");
        this.model.put("bar", "foo");
        replay(this.parameters);
        assertEquals("foo", this.matcher.getMatchString(this.model, this.parameters));
        verify(this.parameters);
    }
}
