package edu.stanford.irt.laneweb.popular;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTBigqueryServiceTest {

    private RESTService restService;

    private RESTBigqueryService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("").toURI();
        this.restService = mock(RESTService.class);
        this.service = new RESTBigqueryService(this.uri, this.restService);
    }

    @Test
    public void testGetList() {
        Map<String, String> map = new HashMap<>();
        map.put("id", "1");
        map.put("title", "title");
        expect(this.restService.getObject(eq(this.uri.resolve("popular/type/resourceType")),
                isA(TypeReference.class))).andReturn(Collections.singletonList(map));
        replay(this.restService);
        List<Map<String, String>> list = this.service.getPopularResources("resourceType");
        Map<String, String> res = list.get(0);
        assertEquals("1", res.get("id"));
        assertEquals("title", res.get("title"));
        verify(this.restService);
    }
}
