package edu.stanford.irt.laneweb.flickr;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;

public class FlickrPhotoList extends ArrayList<FlickrPhoto> {

    private static final long serialVersionUID = 1L;

    public FlickrPhotoList() {
        InputStream input = FlickrPhotoList.class.getResourceAsStream("flickr-photos.txt");
        Reader reader = new InputStreamReader(input);
        BufferedReader br = new BufferedReader(reader);
        br.lines().map(l -> l.split(",")).forEach(s -> add(new FlickrPhoto(s[0], s[1])));
    }
}
