package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.util.Collections;

import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.flickr.FlickrPhotoListService;

public class FlickrPhotoControllerTest {

    private FlickrPhotoController controller;

    private HttpServletResponse response;

    private FlickrPhotoListService service;

    @Before
    public void setUp() {
        this.service = mock(FlickrPhotoListService.class);
        this.controller = new FlickrPhotoController(this.service);
        this.response = mock(HttpServletResponse.class);
    }

    @Test
    public void testGetFlickrPhotoList() {
        this.response.setHeader("Cache-Control", "no-cache");
        expect(this.service.getRandomPhotos(6)).andReturn(Collections.emptyList());
        replay(this.service, this.response);
        assertEquals(Collections.emptyList(), this.controller.getFlickrPhotoList(this.response));
        verify(this.service, this.response);
    }
}
