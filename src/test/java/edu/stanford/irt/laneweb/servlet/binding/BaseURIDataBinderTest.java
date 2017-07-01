package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.assertSame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;

public class BaseURIDataBinderTest {

    private BaseURIDataBinder dataBinder;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = getClass().getResource("/").toURI();
        this.dataBinder = new BaseURIDataBinder("key", this.uri);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        this.dataBinder.bind(model, null);
        assertSame(this.uri, model.get("key"));
    }

    @Test(expected = LanewebException.class)
    public void testNullURL() {
        new BaseURIDataBinder(null, null);
    }
}
