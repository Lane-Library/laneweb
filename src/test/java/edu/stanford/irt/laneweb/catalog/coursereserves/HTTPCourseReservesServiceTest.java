package edu.stanford.irt.laneweb.catalog.coursereserves;

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

import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.LanewebException;

public class HTTPCourseReservesServiceTest {

    private ObjectMapper objectMapper;

    private HTTPCourseReservesService service;

    @Before
    public void setUp() throws URISyntaxException {
        this.objectMapper = mock(ObjectMapper.class);
        this.service = new HTTPCourseReservesService(this.objectMapper, getClass().getResource("").toURI());
    }

    @Test
    public void testGetCourses() throws IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andReturn(Collections.emptyList());
        replay(this.objectMapper);
        assertSame(Collections.emptyList(), this.service.getCourses());
        verify(this.objectMapper);
    }

    @Test(expected = LanewebException.class)
    public void testGetCoursesThrowsException() throws IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(TypeReference.class)))
                .andThrow(new IOException());
        replay(this.objectMapper);
        this.service.getCourses();
    }

    @Test
    public void testGetItems() throws IOException {
        CourseReservesItemList list = mock(CourseReservesItemList.class);
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andReturn(list);
        replay(this.objectMapper);
        assertSame(list, this.service.getItems());
        verify(this.objectMapper);
    }

    @Test
    public void testGetItemsInt() throws IOException {
        CourseReservesItemList list = mock(CourseReservesItemList.class);
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andReturn(list);
        replay(this.objectMapper);
        assertSame(list, this.service.getItems(1));
        verify(this.objectMapper);
    }

    @Test(expected = LanewebException.class)
    public void testGetItemsIntThrowsException() throws IOException {
        mock(CourseReservesItemList.class);
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andThrow(new IOException());
        replay(this.objectMapper);
        this.service.getItems(1);
    }

    @Test(expected = LanewebException.class)
    public void testGetItemsThrowsException() throws IOException {
        mock(CourseReservesItemList.class);
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andThrow(new IOException());
        replay(this.objectMapper);
        this.service.getItems();
    }
}
