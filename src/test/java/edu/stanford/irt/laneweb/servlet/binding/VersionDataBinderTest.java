package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class VersionDataBinderTest {

    private VersionDataBinder binder;

    @Before
    public void setUp() throws Exception {
        this.binder = new VersionDataBinder("version");
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<String, Object>();
        this.binder.bind(model, null);
        assertEquals("version", model.get(Model.VERSION));
    }
}
