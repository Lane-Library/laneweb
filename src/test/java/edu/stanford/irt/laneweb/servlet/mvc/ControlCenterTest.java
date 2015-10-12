package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import org.junit.Before;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.email.SpamFilter;
import edu.stanford.irt.laneweb.servlet.binding.UserDataBinder;

public class ControlCenterTest {

    private UserDataBinder binder;

    private ApplicationContext context;

    private ControlCenter control;

    private Model model;

    private SpamFilter spamFilter;

    @Before
    public void setUp() throws Exception {
        this.binder = createMock(UserDataBinder.class);
        this.context = createMock(ApplicationContext.class);
        this.control = new ControlCenter(this.context, this.binder);
        this.model = createMock(Model.class);
        this.spamFilter = createMock(SpamFilter.class);
    }

    @Test
    public void testAddSpamIP() {
        expect(this.context.getBean(SpamFilter.class)).andReturn(this.spamFilter);
        this.spamFilter.addSpamIP("127.0.0.1");
        replay(this.context, this.spamFilter);
        assertEquals("127.0.0.1", this.control.addSpamIP("127.0.0.1", "ceyates@stanford.edu"));
        verify(this.context, this.spamFilter);
    }

    @Test
    public void testAddSpamIPNotAuthorized() {
        try {
            this.control.addSpamIP("127.0.0.1", "ditenus");
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testAddSpamReferrer() {
        expect(this.context.getBean(SpamFilter.class)).andReturn(this.spamFilter);
        this.spamFilter.addSpamReferrer("referrer");
        replay(this.context, this.spamFilter);
        assertEquals("referrer", this.control.addSpamReferrer("referrer", "ceyates@stanford.edu"));
        verify(this.context, this.spamFilter);
    }

    @Test
    public void testAddSpamReferrerNotAuthorized() {
        try {
            this.control.addSpamReferrer("referrer", "ditenus");
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testGetParameters() {
        expect(this.model.asMap()).andReturn(null);
        this.binder.bind(null, null);
        replay(this.model);
        this.control.getParameters(null, this.model);
        verify(this.model);
    }

    @Test
    public void testRemoveSpamIP() {
        expect(this.context.getBean(SpamFilter.class)).andReturn(this.spamFilter);
        expect(this.spamFilter.removeSpamIP("127.0.0.1")).andReturn(true);
        replay(this.context, this.spamFilter);
        assertTrue(this.control.removeSpamIP("127.0.0.1", "ceyates@stanford.edu"));
        verify(this.context, this.spamFilter);
    }

    @Test
    public void testRemoveSpamIPNotAuthorized() {
        try {
            this.control.removeSpamIP("127.0.0.1", "ditenus");
            fail();
        } catch (LanewebException e) {
        }
    }

    @Test
    public void testRemoveSpamReferrer() {
        expect(this.context.getBean(SpamFilter.class)).andReturn(this.spamFilter);
        expect(this.spamFilter.removeSpamReferrer("referrer")).andReturn(true);
        replay(this.context, this.spamFilter);
        assertTrue(this.control.removeSpamReferrer("referrer", "ceyates@stanford.edu"));
        verify(this.context, this.spamFilter);
    }

    @Test
    public void testRemoveSpamRefrrerNotAuthorized() {
        try {
            this.control.removeSpamReferrer("referrer", "ditenus");
            fail();
        } catch (LanewebException e) {
        }
    }
}
