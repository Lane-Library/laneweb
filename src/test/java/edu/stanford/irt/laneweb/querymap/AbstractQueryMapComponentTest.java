package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;

public class AbstractQueryMapComponentTest {

    private AbstractQueryMapComponent component;

    private Parameters parameters;

    private QueryMapper queryMapper;

    @Before
    public void setUp() throws Exception {
        this.component = new AbstractQueryMapComponent() {
        };
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
    }

    @Test
    public void testSetQueryMapper() {
        try {
            this.component.setQueryMapper(null);
        } catch (IllegalArgumentException e) {
        }
        this.component.setQueryMapper(this.queryMapper);
    }

    @Test
    public void testSetup() {
        try {
            this.component.setup(null, null, null, null);
            fail();
        } catch (IllegalArgumentException e) {
        }
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replay(this.parameters);
        try {
            this.component.setup(null, null, null, this.parameters);
        } catch (IllegalStateException e) {
        }
        this.component.setQueryMapper(this.queryMapper);
        this.component.setup(null, null, null, this.parameters);
        verify(this.parameters);
    }

    @Test
    public void testGetQueryMap() {
        QueryMap queryMap = createMock(QueryMap.class);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        replay(this.queryMapper);
        this.component.setQueryMapper(this.queryMapper);
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replay(this.parameters);
        this.component.setup(null, null, null, this.parameters);
        this.component.getQueryMap();
        verify(this.queryMapper);
        verify(this.parameters);
    }

    @Test
    public void testReset() {
        this.component.setQueryMapper(this.queryMapper);
        expect(this.parameters.getParameter("query", null)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameterAsInteger("abstract-count", 100)).andReturn(null);
        replay(this.parameters);
        this.component.setup(null, null, null, this.parameters);
        this.component.reset();
        try {
            this.component.getQueryMap();
        } catch (IllegalStateException e) {

        }
        verify(this.parameters);
    }

}
