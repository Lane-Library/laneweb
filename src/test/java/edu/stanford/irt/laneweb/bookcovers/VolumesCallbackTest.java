package edu.stanford.irt.laneweb.bookcovers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.google.api.client.googleapis.json.GoogleJsonError;
import com.google.api.services.books.model.Volume;
import com.google.api.services.books.model.Volume.VolumeInfo;
import com.google.api.services.books.model.Volume.VolumeInfo.ImageLinks;
import com.google.api.services.books.model.Volumes;

public class VolumesCallbackTest {

    private VolumesCallback callback;

    private Map<Integer, String> thumbnailMap;

    private Volumes volumes;

    @Before
    public void setUp() {
        this.thumbnailMap = new HashMap<>();
        this.callback = new VolumesCallback(Integer.valueOf(12), this.thumbnailMap);
        this.volumes = new Volumes();
    }

    @Test
    public void testOnError() {
        this.callback.onFailure(new GoogleJsonError(), null);
    }

    @Test
    public void testOnSuccessItemAlreadyPresent() {
        this.volumes.setTotalItems(Integer.valueOf(1));
        this.thumbnailMap.put(12, null);
        this.callback.onSuccess(this.volumes, null);
        assertEquals(1, this.thumbnailMap.size());
    }

    @Test
    public void testOnSuccessNoImageLinks() {
        this.volumes.setTotalItems(Integer.valueOf(1));
        Volume volume = new Volume();
        volume.set("volumeInfo", new Volume.VolumeInfo());
        this.volumes.put("items", Collections.singletonList(volume));
        this.callback.onSuccess(this.volumes, null);
        assertEquals(null, this.thumbnailMap.get(12));
    }

    @Test
    public void testOnSuccessNoItems() {
        this.volumes.setTotalItems(Integer.valueOf(0));
        this.callback.onSuccess(this.volumes, null);
        assertTrue(this.thumbnailMap.isEmpty());
    }

    @Test
    public void testOnSuccessNoThumbnails() {
        this.volumes.setTotalItems(Integer.valueOf(1));
        Volume volume = new Volume();
        VolumeInfo volumeInfo = new VolumeInfo();
        volumeInfo.set("imageLinks", new ImageLinks());
        volume.set("volumeInfo", volumeInfo);
        this.volumes.put("items", Collections.singletonList(volume));
        this.callback.onSuccess(this.volumes, null);
        assertEquals(null, this.thumbnailMap.get(12));
    }

    @Test
    public void testOnSuccessNoVolumeInfo() {
        this.volumes.setTotalItems(Integer.valueOf(1));
        this.volumes.put("items", Collections.singletonList(new Volume()));
        this.callback.onSuccess(this.volumes, null);
        assertEquals(null, this.thumbnailMap.get(12));
    }

    @Test
    public void testOnSuccessOneThumbnail() {
        this.volumes.setTotalItems(Integer.valueOf(1));
        Volume volume = new Volume();
        VolumeInfo volumeInfo = new VolumeInfo();
        ImageLinks imageLinks = new ImageLinks();
        imageLinks.set("smallThumbnail", "thumbnail");
        volumeInfo.set("imageLinks", imageLinks);
        volume.set("volumeInfo", volumeInfo);
        this.volumes.put("items", Collections.singletonList(volume));
        this.callback.onSuccess(this.volumes, null);
        assertEquals("thumbnail", this.thumbnailMap.get(12));
    }
}
