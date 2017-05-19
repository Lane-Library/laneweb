package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.assertSame;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;

public class ContentBaseDataBinderTest {

    private URL contentBase;

    private ContentBaseDataBinder dataBinder;

    @Before
    public void setUp() {
        this.contentBase = getClass().getResource("/");
        this.dataBinder = new ContentBaseDataBinder(this.contentBase);
    }

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        this.dataBinder.bind(model, null);
        assertSame(this.contentBase, model.get(Model.CONTENT_BASE));
    }

    @Test(expected = LanewebException.class)
    public void testNullURL() {
        new ContentBaseDataBinder(null);
    }
}
