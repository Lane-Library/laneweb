package edu.stanford.irt.laneweb.querymap;

import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.expect;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

import edu.emory.mathcs.backport.java.util.Collections;
import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.ResourceMap;

public class JSONableQueryMapTest {
    
    private static final String JSON = //"{\"query\":\"tetralogy of fallot\",\"descriptor\":\"Tetralogy of Fallot\",\"resourceMap\":{\"descriptor\":\"Heart Defects, Congenital\",\"resources\":[{\"id\":\"mdc_park\",\"label\":\"Pediatric Cardiology\"}]}}";
        "{\"query\":\"tetralogy of fallot\",\"descriptor\":\"Tetralogy of Fallot\",\"resourceMap\":{\"descriptor\":\"Heart Defects, Congenital\",\"resources\":[{\"id\":\"mdc_park\"}]}}";
    
    @Test
    public void testToString() {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("Tetralogy of Fallot");
        expect(descriptor.getDescriptorName()).andReturn("Heart Defects, Congenital");
        replay(descriptor);
        ResourceMap map = createMock(ResourceMap.class);
        expect(map.getDescriptor()).andReturn(descriptor);
        expect(map.getResources()).andReturn(Collections.singleton("mdc_park"));
        replay(map);
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy of fallot");
        expect(qm.getDescriptor()).andReturn(descriptor);
        expect(qm.getResourceMap()).andReturn(map);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals(JSON, queryMap.toString());
        verify(descriptor);
        verify(map);
        verify(qm);
    }
    
    @Test
    public void testNoDescriptor() {
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy of fallot");
        expect(qm.getDescriptor()).andReturn(null);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals("{\"query\":\"tetralogy of fallot\"}", queryMap.toString());
        verify(qm);
    }
    
    @Test
    public void testNullMap() {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("Tetralogy of Fallot");
        replay(descriptor);
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy of fallot");
        expect(qm.getDescriptor()).andReturn(descriptor);
        expect(qm.getResourceMap()).andReturn(null);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals("{\"query\":\"tetralogy of fallot\",\"descriptor\":\"Tetralogy of Fallot\"}", queryMap.toString());
        verify(descriptor);
        verify(qm);
    }
    
    @Test
    public void testApostrophe() {
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy o' fallot");
        expect(qm.getDescriptor()).andReturn(null);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals("{\"query\":\"tetralogy o\\' fallot\"}", queryMap.toString());
        verify(qm);
    }
    
    @Test
    public void testQuote() {
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy o\" fallot");
        expect(qm.getDescriptor()).andReturn(null);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals("{\"query\":\"tetralogy o\\\" fallot\"}", queryMap.toString());
        verify(qm);
    }

}
