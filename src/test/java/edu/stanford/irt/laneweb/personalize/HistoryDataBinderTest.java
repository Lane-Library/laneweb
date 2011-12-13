package edu.stanford.irt.laneweb.personalize;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class HistoryDataBinderTest {
    
    private HistoryDataBinder binder;
    
    private Map<String, Object> model;
    
    private List<History> history;
    
    private BookmarkDAO<History> dao;
    
    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.binder = new HistoryDataBinder();
        this.model = new HashMap<String, Object>();
        this.model.put(Model.SUNETID, "ditenus");
        this.history = Collections.emptyList();
        this.dao = createMock(BookmarkDAO.class);
        this.binder.setHistoryDAO(this.dao);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.HISTORY)).andReturn(null);
        expect(this.dao.getLinks("ditenus")).andReturn(this.history);
        this.session.setAttribute(Model.HISTORY, this.history);
        replay(this.dao, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.history, this.model.get(Model.HISTORY));
        verify(this.dao, this.request, this.session);
    }
}
