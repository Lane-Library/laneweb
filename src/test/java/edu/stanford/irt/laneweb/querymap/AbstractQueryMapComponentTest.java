package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.fail;

import org.apache.avalon.framework.parameters.Parameters;
import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.LanewebObjectModel;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;

public class AbstractQueryMapComponentTest {

    private AbstractQueryMapComponent component;

    private Parameters parameters;

    private QueryMapper queryMapper;
    
    private Model model;

    @Before
    public void setUp() throws Exception {
        this.component = new AbstractQueryMapComponent() {
        };
        this.parameters = createMock(Parameters.class);
        this.queryMapper = createMock(QueryMapper.class);
        this.model = createMock(Model.class);
        this.component.setModel(this.model);
    }

    @Test
    public void testGetQueryMap() {
        QueryMap queryMap = createMock(QueryMap.class);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(queryMap);
        this.component.setQueryMapper(this.queryMapper);
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        replayMocks();
        this.component.setup(null, null, null, this.parameters);
        this.component.getQueryMap();
        verifyMocks();
    }

    @Test
    public void testReset() {
        this.component.setQueryMapper(this.queryMapper);
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        expect(this.queryMapper.getQueryMap("dvt")).andReturn(null);
        replayMocks();
        this.component.setup(null, null, null, this.parameters);
        try {
            this.component.getQueryMap();
        } catch (IllegalStateException e) {
        }
        verifyMocks();
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
        } catch (IllegalStateException e) {
        }
        expect(this.model.getString(LanewebObjectModel.QUERY)).andReturn("dvt");
        expect(this.parameters.getParameter("resource-maps", null)).andReturn(null);
        expect(this.parameters.getParameter("descriptor-weights", null)).andReturn(null);
        expect(this.parameters.getParameter("abstract-count", null)).andReturn(null);
        replayMocks();
        try {
            this.component.setup(null, null, null, this.parameters);
        } catch (IllegalStateException e) {
        }
        this.component.setQueryMapper(this.queryMapper);
        this.component.setup(null, null, null, this.parameters);
        verifyMocks();
    }
    
    private void replayMocks() {
        replay(this.model);
        replay(this.parameters);
        replay(this.queryMapper);
    }
    
    private void verifyMocks() {
        verify(this.model);
        verify(this.parameters);
        verify(this.queryMapper);
    }
}
