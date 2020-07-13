package edu.stanford.irt.laneweb.servlet.binding;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.livechat.LiveChatAvailabilityService;
import edu.stanford.irt.laneweb.model.Model;

public class LiveChatAvailabilityBinderTest {

    private LiveChatAvailabilityBinder dataBinder;

    private LiveChatAvailabilityService service;

    @Before
    public void setUp() {
        this.service = mock(LiveChatAvailabilityService.class);
        this.dataBinder = new LiveChatAvailabilityBinder(this.service);
    }

    @Test
    public void testBind() {
        expect(this.service.isAvailable()).andReturn(true);
        Map<String, Object> model = new HashMap<>();
        replay(this.service);
        this.dataBinder.bind(model, null);
        assertSame(Boolean.TRUE, model.get(Model.LIVE_CHAT_AVAILABLE));
        verify(this.service);
    }
}
