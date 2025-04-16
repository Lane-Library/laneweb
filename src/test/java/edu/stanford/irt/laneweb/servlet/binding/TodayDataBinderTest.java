package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class TodayDataBinderTest {

    @Test
    public void testBind() {
        Map<String, Object> model = new HashMap<>();
        new TodayDataBinder().bind(model, null);
        assertEquals(LocalDate.now(ZoneId.of("America/Los_Angeles")), model.get(Model.TODAY));
    }
}
