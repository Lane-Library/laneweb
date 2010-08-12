package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.classextension.EasyMock.createMock;
import static org.easymock.classextension.EasyMock.replay;
import static org.easymock.classextension.EasyMock.verify;

import java.util.Collections;

import javax.servlet.http.HttpServletRequest;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.junit.Before;
import org.junit.Test;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import edu.stanford.irt.laneweb.model.Model;

public class ServletObjectModelProviderTest {

    private ObjectModelProvider provider;

    private HttpServletRequest request;

    @Before
    public void setUp() throws Exception {
        this.request = createMock(HttpServletRequest.class);
        this.provider = new RequestAttributeObjectModelProvider();
        RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(this.request));
    }

    @Test
    public void testGetObject() {
        expect(this.request.getAttribute(Model.MODEL)).andReturn(Collections.emptyMap());
        replayMocks();
        this.provider.getObject();
        verifyMocks();
    }

    private void replayMocks() {
        replay(this.request);
    }

    private void verifyMocks() {
        verify(this.request);
    }
}
