package edu.stanford.irt.laneweb.servlet.binding;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkingDataBinderTest {

    private Map<String, Object> model;

    @BeforeEach
    public void setUp() {
        this.model = new HashMap<>();
    }

    @Test
    public void testBindNull() {
        new BookmarkingDataBinder(null).bind(this.model, null);
        assertEquals("off", this.model.get(Model.BOOKMARKING));
    }

    @Test
    public void testBindOFF() {
        new BookmarkingDataBinder("off").bind(this.model, null);
        assertEquals("off", this.model.get(Model.BOOKMARKING));
    }

    @Test
    public void testBindRO() {
        new BookmarkingDataBinder("ro").bind(this.model, null);
        assertEquals("ro", this.model.get(Model.BOOKMARKING));
    }

    @Test
    public void testBindRW() {
        new BookmarkingDataBinder("rw").bind(this.model, null);
        assertEquals("rw", this.model.get(Model.BOOKMARKING));
    }
}
