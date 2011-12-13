package edu.stanford.irt.laneweb.personalize;

import static org.junit.Assert.*;
import static org.easymock.EasyMock.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.easymock.Capture;
import org.junit.Before;
import org.junit.Test;

public class HistoryTrackerControllerTest {
    
    private HistoryTrackerController controller;
    private List<History> historyList;
    private BookmarkDAO<History> dao;
    private History history;

    @Before
    public void setUp() throws Exception {
        this.controller = new HistoryTrackerController();
        this.historyList = new ArrayList<History>();
        this.dao = createMock(BookmarkDAO.class);
        this.controller.setBookmarkDAO(this.dao);
        this.history = new History("label","url",new Date(Long.MAX_VALUE));
    }

    @Test
    public void testGetHistoryListOfHistoryLink() {
        assertEquals(this.historyList, this.controller.getHistory(this.historyList));
    }

    @Test
    public void testGetHistoryString() {
        expect(this.dao.getLinks("ditenus")).andReturn(this.historyList);
        replay(this.dao);
        assertEquals(this.historyList, this.controller.getHistory("ditenus"));
        verify(this.dao);
    }

    @Test
    public void testTrackHistory() {
        Capture<List<History>> historyCapture = new Capture<List<History>>();
        this.dao.saveLinks(eq("ditenus"), capture(historyCapture));
        replay(this.dao);
        this.controller.trackHistory(this.history, "ditenus", this.historyList);
        assertEquals(this.history, historyCapture.getValue().get(0));
        verify(this.dao);
    }
}
