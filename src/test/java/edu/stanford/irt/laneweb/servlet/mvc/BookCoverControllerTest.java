package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.bookcovers.BookCoverService;

public class BookCoverControllerTest {

    private BookCoverController controller;

    private BookCoverService service;

    @BeforeEach
    public void setUp() {
        this.service = mock(BookCoverService.class);
        this.controller = new BookCoverController(this.service);
    }

    @Test
    public void testGetBookCovers() {
        expect(this.service.getBookCoverURLs(Collections.singletonList("bib-12"))).andReturn(Collections.emptyMap());
        replay(this.service);
        assertSame(Collections.emptyMap(), this.controller.getBookCovers(Collections.singletonList("bib-12")));
        verify(this.service);
    }
}
