package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.hours.TodaysHours;
import edu.stanford.irt.laneweb.model.Model;

public class TodaysHoursBinderTest {

    private TodaysHoursBinder binder;

    private TodaysHours hours;

    private Map<String, Object> model;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.hours = createMock(TodaysHours.class);
        this.binder = new TodaysHoursBinder(this.hours);
        this.model = new HashMap<>();
        this.request = createMock(HttpServletRequest.class);
    }

    @Test
    public void testBind() {
        expect(this.hours.getHours()).andReturn("baz");
        replay(this.hours);
        this.binder.bind(this.model, this.request);
        assertEquals("baz", this.model.get(Model.TODAYS_HOURS));
        verify(this.hours);
    }

    @Test
    public void testBindThrowsException() {
        LanewebException e = new LanewebException("oopsie");
        expect(this.hours.getHours()).andThrow(e);
        replay(this.hours);
        this.binder.bind(this.model, this.request);
        assertEquals("??", this.model.get(Model.TODAYS_HOURS));
        verify(this.hours);
    }
}
