package edu.stanford.irt.laneweb.catalog.equipment;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTEquipmentServiceTest {

    private BasicAuthRESTService restService;

    private RESTEquipmentService service;

    private URI uri;

    @BeforeEach
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.restService = mock(BasicAuthRESTService.class);
        this.service = new RESTEquipmentService(this.uri, this.restService);
    }

    @Test
    public void testGetList() {
        Map<String, String> map = new HashMap<>();
        map.put("bibID", "1");
        map.put("count", "1");
        map.put("title", "title");
        map.put("note", "note");
        expect(this.restService.getObject(eq(this.uri.resolve("equipment/list")), isA(TypeReference.class)))
                .andReturn(Collections.singletonList(map));
        replay(this.restService);
        List<Equipment> list = this.service.getList();
        Equipment e = list.get(0);
        assertEquals("1", e.getBibID());
        assertEquals("1", e.getCount());
        assertEquals("title", e.getTitle());
        assertEquals("note", e.getNote());
        verify(this.restService);
    }
}
