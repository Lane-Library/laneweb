package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.livechat.Schedule;
import edu.stanford.irt.laneweb.model.Model;

public class LiveChatScheduleBinderTest {

    private LiveChatScheduleBinder dataBinder;

    private Schedule schedule;

    @Before
    public void setUp() {
        this.schedule = createMock(Schedule.class);
        this.dataBinder = new LiveChatScheduleBinder(this.schedule);
    }

    @Test
    public void testBind() {
        expect(this.schedule.isAvailable()).andReturn(true);
        Map<String, Object> model = new HashMap<>();
        replay(this.schedule);
        this.dataBinder.bind(model, null);
        assertSame(Boolean.TRUE, model.get(Model.LIVE_CHAT_AVAILABLE));
        verify(this.schedule);
    }
}
