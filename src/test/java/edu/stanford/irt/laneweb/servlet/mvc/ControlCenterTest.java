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
import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

public class ControlCenterTest {

    private SunetIdAndTicketDataBinder binder;

    private ApplicationContext context;

    private ControlCenter control;

    private Model model;

    private EMailSender sender;

    @Before
    public void setUp() throws Exception {
        this.binder = createMock(SunetIdAndTicketDataBinder.class);
        this.context = createMock(ApplicationContext.class);
        this.control = new ControlCenter(this.context, this.binder);
        this.model = createMock(Model.class);
        this.sender = createMock(EMailSender.class);
    }

    @Test
    public void testAddSpamIP() {
        expect(this.context.getBean(EMailSender.class)).andReturn(this.sender);
        this.sender.addSpamIP("127.0.0.1");
        replay(this.context, this.sender);
        assertEquals("127.0.0.1", this.control.addSpamIP("127.0.0.1", "ceyates"));
        verify(this.context, this.sender);
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
    public void testGetParameters() {
        expect(this.model.asMap()).andReturn(null);
        this.binder.bind(null, null);
        replay(this.model);
        this.control.getParameters(null, this.model);
        verify(this.model);
    }

    @Test
    public void testRemoveSpamIP() {
        expect(this.context.getBean(EMailSender.class)).andReturn(this.sender);
        expect(this.sender.removeSpamIP("127.0.0.1")).andReturn(true);
        replay(this.context, this.sender);
        assertTrue(this.control.removeSpamIP("127.0.0.1", "ceyates"));
        verify(this.context, this.sender);
    }

    @Test
    public void testRemoveSpamIPNotAuthorized() {
        try {
            this.control.removeSpamIP("127.0.0.1", "ditenus");
            fail();
        } catch (LanewebException e) {
        }
    }
}
