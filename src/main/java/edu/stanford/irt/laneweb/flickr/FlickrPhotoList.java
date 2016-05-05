package edu.stanford.irt.laneweb.flickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

import edu.stanford.irt.laneweb.LanewebException;

public class FlickrPhotoList extends ArrayList<FlickrPhoto> {

    private static final long serialVersionUID = 1L;

    public FlickrPhotoList() {
        try (BufferedReader br = new BufferedReader(
                new InputStreamReader(getClass().getResourceAsStream("flickr-photos.txt"), StandardCharsets.UTF_8))) {
            br.lines().map(l -> l.split(",")).forEach(s -> add(new FlickrPhoto(s[0], s[1])));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
