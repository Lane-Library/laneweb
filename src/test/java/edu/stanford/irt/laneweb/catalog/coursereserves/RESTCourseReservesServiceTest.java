package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.eq;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;

public class RESTCourseReservesServiceTest {

    private CourseReservesItemList itemList;
    
    private BasicAuthRESTService restService;

    private RESTCourseReservesService service;

    private URI uri;

    @Before
    public void setUp() throws URISyntaxException {
        this.uri = new URI("/");
        this.restService = mock(BasicAuthRESTService.class);
        this.service = new RESTCourseReservesService(this.uri, this.restService);
        this.itemList = mock(CourseReservesItemList.class);
    }

    @Test
    public void testGetCourses() throws URISyntaxException {
        expect(this.restService.getObject(eq(new URI("/coursereserves/courses")), isA(TypeReference.class)))
                .andReturn(Collections.emptyList());
        replay(this.restService);
        assertSame(Collections.emptyList(), this.service.getCourses());
        verify(this.restService);
    }

    @Test
    public void testGetItems() throws URISyntaxException {
        expect(this.restService.getObject(eq(new URI("/coursereserves/items")), same(CourseReservesItemList.class)))
                .andReturn(this.itemList);
        replay(this.restService);
        assertSame(this.itemList, this.service.getItems());
        verify(this.restService);
    }

    @Test
    public void testGetItemsInt() throws URISyntaxException {
        expect(this.restService.getObject(eq(new URI("/coursereserves/items?id=0")),
                same(CourseReservesItemList.class))).andReturn(this.itemList);
        replay(this.restService);
        assertSame(this.itemList, this.service.getItems("0"));
        verify(this.restService);
    }
}
