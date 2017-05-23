package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class DisasterModeDataBinderTest {

    private DisasterModeDataBinder binder;

    @Test
    public void testFalse() {
        this.binder = new DisasterModeDataBinder(Boolean.FALSE);
        Map<String, Object> model = new HashMap<>();
        this.binder.bind(model, null);
        assertFalse(ModelUtil.getObject(model, Model.DISASTER_MODE, Boolean.class));
    }

    @Test
    public void testNull() {
        this.binder = new DisasterModeDataBinder(null);
        Map<String, Object> model = new HashMap<>();
        this.binder.bind(model, null);
        assertFalse(ModelUtil.getObject(model, Model.DISASTER_MODE, Boolean.class));
    }

    @Test
    public void testTrue() {
        this.binder = new DisasterModeDataBinder(Boolean.TRUE);
        Map<String, Object> model = new HashMap<>();
        this.binder.bind(model, null);
        assertTrue(ModelUtil.getObject(model, Model.DISASTER_MODE, Boolean.class));
    }
}
