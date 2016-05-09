package edu.stanford.irt.laneweb.flickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;

public class FlickrPhotoList extends ArrayList<FlickrPhoto> {

    private static final long serialVersionUID = 1L;

    public FlickrPhotoList(final InputStream input) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            br.lines().map(l -> l.split(",")).forEach(s -> add(new FlickrPhoto(s[0], s[1])));
        }
    }
}
