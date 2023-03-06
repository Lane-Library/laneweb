package edu.stanford.irt.laneweb.flickr;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class FlickrPhotoListCreatorTest {

    private static final String UTF_8 = StandardCharsets.UTF_8.name();

    private FlickrPhotoListCreator creator;

    private String expected = "https://www.flickr.com/photos/OWNER/ID\thttps://farmFARM.staticflickr.com/SERVER/ID_SECRET_m.jpg\ttitle\n"
            + "https://www.flickr.com/photos/OWNER/ID\thttps://farmFARM.staticflickr.com/SERVER/ID_SECRET_m.jpg\ttitle\n";

    private ObjectMapper objectMapper;

    private Object photo;

    @Before
    public void setUp() throws IOException {
        this.objectMapper = mock(ObjectMapper.class);
        this.creator = new FlickrPhotoListCreator("apiKey", "file:/", this.objectMapper);
        this.photo = mock(FlickrPhoto.class);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void testPrintList() throws IOException {
        Map<String, String> photoMap = new HashMap<>();
        photoMap.put("owner", "OWNER");
        photoMap.put("id", "ID");
        photoMap.put("farm", "FARM");
        photoMap.put("server", "SERVER");
        photoMap.put("secret", "SECRET");
        photoMap.put("title", "title");
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class))).andReturn(Collections
                .singletonMap("photos", Collections.singletonMap("photo", Collections.singletonList(photoMap))))
                .times(2);
        replay(this.objectMapper, this.photo);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream pw = new PrintStream(baos);
        this.creator.printList(pw);
        assertEquals(this.expected, baos.toString(UTF_8));
        verify(this.objectMapper, this.photo);
    }

    @SuppressWarnings("unchecked")
    @Test(expected = LanewebException.class)
    public void testPrintListError() throws IOException {
        expect(this.objectMapper.readValue(isA(InputStream.class), isA(Class.class)))
                .andReturn(Collections.singletonMap("message", "oopsie"));
        replay(this.objectMapper);
        this.creator.printList(null);
    }
}
