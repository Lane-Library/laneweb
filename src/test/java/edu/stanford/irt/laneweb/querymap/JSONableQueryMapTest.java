package edu.stanford.irt.laneweb.querymap;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

import org.junit.Test;

import edu.stanford.irt.querymap.Descriptor;
import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.Resource;
import edu.stanford.irt.querymap.ResourceMap;

public class JSONableQueryMapTest {

    private static final String JSON_1 = "{\"query\":\"tetralogy of fallot\",\"descriptor\":\"Tetralogy of Fallot\",\"resourceMap\":{\"descriptor\":\"Heart Defects, Congenital\",\"resources\":[{\"id\":\"mdc_park\",\"label\":\"Pediatric Cardiology\"}]}}";

    private static final String JSON_2 = "{\"query\":\"borderline personality\",\"descriptor\":\"Borderline Personality Disorder\",\"resourceMap\":{\"descriptor\":\"Mental Disorders\",\"resources\":[{\"id\":\"ovid-kaplan\",\"label\":\"Kaplan's Comprehensive Psychiatry\"},{\"id\":\"am_ebert\",\"label\":\"Current Dx & Tx: Psychiatry\"}]}}";

    // "{\"query\":\"tetralogy of fallot\",\"descriptor\":\"Tetralogy of Fallot\",\"resourceMap\":{\"descriptor\":\"Heart Defects, Congenital\",\"resources\":[{\"id\":\"mdc_park\"}]}}";
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
    public void testQuote() {
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy o\" fallot");
        expect(qm.getDescriptor()).andReturn(null);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals("{\"query\":\"tetralogy o\\\" fallot\"}", queryMap.toString());
        verify(qm);
    }

    @Test
    public void testToString1() {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("Tetralogy of Fallot");
        expect(descriptor.getDescriptorName()).andReturn("Heart Defects, Congenital");
        replay(descriptor);
        ResourceMap map = createMock(ResourceMap.class);
        expect(map.getDescriptor()).andReturn(descriptor);
        expect(map.getResources()).andReturn(
                Collections.<Resource> singleton(new Resource("mdc_park", "Pediatric Cardiology")));
        replay(map);
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("tetralogy of fallot");
        expect(qm.getDescriptor()).andReturn(descriptor);
        expect(qm.getResourceMap()).andReturn(map);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals(JSON_1, queryMap.toString());
        verify(descriptor);
        verify(map);
        verify(qm);
    }

    @Test
    public void testToString2() {
        Descriptor descriptor = createMock(Descriptor.class);
        expect(descriptor.getDescriptorName()).andReturn("Borderline Personality Disorder");
        expect(descriptor.getDescriptorName()).andReturn("Mental Disorders");
        replay(descriptor);
        ResourceMap map = createMock(ResourceMap.class);
        expect(map.getDescriptor()).andReturn(descriptor);
        Set<Resource> resources = new LinkedHashSet<Resource>();
        resources.add(new Resource("ovid-kaplan", "Kaplan's Comprehensive Psychiatry"));
        resources.add(new Resource("am_ebert", "Current Dx & Tx: Psychiatry"));
        expect(map.getResources()).andReturn(resources);
        replay(map);
        QueryMap qm = createMock(QueryMap.class);
        expect(qm.getQuery()).andReturn("borderline personality");
        expect(qm.getDescriptor()).andReturn(descriptor);
        expect(qm.getResourceMap()).andReturn(map);
        replay(qm);
        JSONableQueryMap queryMap = new JSONableQueryMap(qm);
        assertEquals(JSON_2, queryMap.toString());
        verify(descriptor);
        verify(map);
        verify(qm);
    }
}
