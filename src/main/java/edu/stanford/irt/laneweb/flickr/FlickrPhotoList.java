package edu.stanford.irt.laneweb.flickr;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Function;

public class FlickrPhotoList extends ArrayList<FlickrPhoto> {

    private static final long serialVersionUID = 1L;

    private static Function<String, String[]> split = (final String s) -> s.split("\t");

    public FlickrPhotoList(final InputStream input) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            br.lines().map(split).forEach((final String[] s) -> add(new FlickrPhoto(s[0], s[1], s[2])));
        }
    }
}
