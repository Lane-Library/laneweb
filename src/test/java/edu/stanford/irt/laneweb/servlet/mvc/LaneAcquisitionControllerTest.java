package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

public class LaneAcquisitionControllerTest {

    private LaneAcquisitionController controller;

    @Before
    public void setUp() {
        this.controller = new LaneAcquisitionController();
    }

    @Test
    public void testFormSubmitLanelibacqs() throws IOException {
        Model model = createMock(Model.class);
        expect(model.asMap()).andReturn(Collections.singletonMap("foo", "bar"));
        replay(model);
        assertEquals("redirect:/index.html", this.controller.formSubmitLanelibacqs(model, null));
    }

    @Test
    public void testJsonSubmitLanelibacqs() throws IOException {
        this.controller.jsonSubmitLanelibacqs(Collections.singletonMap("foo", "bar"));
    }
}
