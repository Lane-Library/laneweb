package edu.stanford.irt.laneweb.bookcovers;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class BookCoverServiceTest {

    private ObjectMapper objectMapper;

    private BookCoverService service;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.service = new BookCoverService(this.objectMapper, getClass().getResource(".").toURI());
    }

    @Test
    public void testGetBookCoverURLs() throws IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andReturn(Collections.emptyMap());
        replay(this.objectMapper);
        assertSame(Collections.emptyMap(), this.service.getBookCoverURLs(Collections.singleton(Integer.valueOf(12))));
        verify(this.objectMapper);
    }

    @Test(expected = LanewebException.class)
    public void testGetBookCoverURLsThrowsException() throws IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andThrow(new IOException());
        replay(this.objectMapper);
        this.service.getBookCoverURLs(Collections.singleton(Integer.valueOf(12)));
    }
}
