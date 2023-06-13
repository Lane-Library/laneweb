package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTEquipmentServiceTest {

    private BasicAuthRESTService restService;

    private RESTEquipmentService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.restService = mock(RESTService.class);
        this.service = new RESTEquipmentService(this.uri, this.restService);
    }

    @Test
    public void testGetRecords() throws IOException {
        expect(this.restService.getInputStream(this.uri.resolve("equipment/records")))
                .andReturn(getClass().getResourceAsStream(("equipment/records")));
        replay(this.restService);
        assertEquals(-1, this.service.getRecords(Collections.emptyList()).read());
        verify(this.restService);
    }

    @Test
    public void testGetStatus() {
        Map<String, String> map = new HashMap<>();
        map.put("bibID", "1");
        map.put("count", "1");
        expect(this.restService.getObject(eq(this.uri.resolve("equipment/status?idList=1")), isA(TypeReference.class)))
                .andReturn(Collections.singletonList(map));
        replay(this.restService);
        EquipmentStatus status = this.service.getStatus("1").get(0);
        assertEquals("1", status.getBibID());
        assertEquals("1", status.getCount());
        verify(this.restService);
    }
}
