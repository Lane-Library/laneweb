package edu.stanford.irt.laneweb.drupal;

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

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class DrupalAPIServiceTest {

    private DrupalAPIService apiService;

    private RESTService restService;

    @Before
    public void setUp() throws URISyntaxException {
        this.restService = mock(RESTService.class);
        this.apiService = new DrupalAPIService(new URI("/"), this.restService);
    }

    @Test
    public void testGetNodeContent() throws RESTException, URISyntaxException {
        Map<String, List<Map<String, String>>> json = new HashMap<>();
        json.put("body", Collections.singletonList(Collections.singletonMap("value", "foo")));
        expect(this.restService.getObject(eq(new URI("/node/1?_format=json")), isA(TypeReference.class)))
                .andReturn(json);
        replay(this.restService);
        assertEquals("foo", this.apiService.getNodeContent("node/1"));
        verify(this.restService);
    }
}
