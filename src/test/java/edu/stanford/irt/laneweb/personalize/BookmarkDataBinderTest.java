package edu.stanford.irt.laneweb.personalize;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;

public class BookmarkDataBinderTest {
    
    private BookmarkDataBinder binder;
    
    private Map<String, Object> model;
    
    private List<Bookmark> bookmarks;
    
    private BookmarkDAO<Bookmark> dao;
    
    private HttpServletRequest request;

    private HttpSession session;

    @Before
    public void setUp() throws Exception {
        this.binder = new BookmarkDataBinder();
        this.model = new HashMap<String, Object>();
        this.model.put(Model.SUNETID, "ditenus");
        this.bookmarks = Collections.emptyList();
        this.dao = createMock(BookmarkDAO.class);
        this.binder.setBookmarkDAO(this.dao);
        this.request = createMock(HttpServletRequest.class);
        this.session = createMock(HttpSession.class);
    }

    @Test
    public void testBind() {
        expect(this.request.getSession()).andReturn(this.session);
        expect(this.session.getAttribute(Model.BOOKMARKS)).andReturn(null);
        expect(this.dao.getLinks("ditenus")).andReturn(this.bookmarks);
        this.session.setAttribute(Model.BOOKMARKS, this.bookmarks);
        replay(this.dao, this.request, this.session);
        this.binder.bind(this.model, this.request);
        assertEquals(this.bookmarks, this.model.get(Model.BOOKMARKS));
        verify(this.dao, this.request, this.session);
    }
}
