package edu.stanford.irt.laneweb.popular;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.cocoon.cache.Validity;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;

public class PopularListGeneratorTest {

    private PopularListGenerator generator;

    private PopularResourcesSAXStrategy saxStrategy;

    private BigqueryService service;

    private XMLConsumer xmlConsumer;

    @BeforeEach
    public void setUp() {
        this.service = mock(BigqueryService.class);
        this.saxStrategy = mock(PopularResourcesSAXStrategy.class);
        this.generator = new PopularListGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() {
        List<Map<String, String>> resources = new ArrayList<>();
        resources.add(new HashMap<>());
        resources.add(new HashMap<>());
        resources.add(new HashMap<>());
        Map<String, String> params = new HashMap<>();
        params.put(Model.TYPE, "type");
        params.put(Model.LIMIT, "2");
        this.generator.setParameters(params);
        expect(this.service.getPopularResources("type")).andReturn(resources);
        this.saxStrategy.toSAX(resources.subList(0, 2), this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateXMLConsumerAll() {
        List<Map<String, String>> resources = new ArrayList<>();
        resources.add(new HashMap<>());
        resources.add(new HashMap<>());
        resources.add(new HashMap<>());
        Map<String, String> params = new HashMap<>();
        params.put(Model.TYPE, "all");
        params.put(Model.LIMIT, "10");
        this.generator.setParameters(params);
        expect(this.service.getAllPopularResources()).andReturn(resources);
        this.saxStrategy.toSAX(resources, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateXMLConsumerEmpty() {
        this.generator.setParameters(Collections.singletonMap(Model.TYPE, "type"));
        expect(this.service.getPopularResources("type")).andReturn(Collections.emptyList());
        this.saxStrategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateXMLConsumerNumberFormatException() {
        Map<String, String> params = new HashMap<>();
        params.put(Model.TYPE, "type");
        params.put(Model.LIMIT, "two");
        this.generator.setParameters(params);
        expect(this.service.getPopularResources("type")).andReturn(Collections.emptyList());
        this.saxStrategy.toSAX(Collections.emptyList(), this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        Map<String, String> params = new HashMap<>();
        params.put(Model.TYPE, "type");
        params.put(Model.LIMIT, "2");
        this.generator.setParameters(params);
        assertEquals("t=type;l=2", this.generator.getKey());
    }

    @Test
    public void testGetKeyAgain() {
        Serializable key = this.generator.getKey();
        assertSame(key, this.generator.getKey());
    }

    @Test
    public void testGetValidity() {
        assertTrue(this.generator.getValidity().isValid());
    }

    @Test
    public void testGetValidityAgain() {
        Validity validity = this.generator.getValidity();
        assertSame(validity, this.generator.getValidity());
    }
}
