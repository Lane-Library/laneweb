package edu.stanford.irt.laneweb.flickr;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class FlickrPhotoListCreator {

    private static final String PAGE = "https://www.flickr.com/photos/%s/%s";

    private static final String THUMBNAIL = "https://farm%s.staticflickr.com/%s/%s_%s_m.jpg";

    private static final String URL_FORMAT = "https://api.flickr.com/services/rest/?method=flickr.people.getPublicPhotos&user_id=40390680@N08&api_key=%s&per_page=500&format=json&nojsoncallback=1&page=%%d";

    private ObjectMapper objectMapper;

    private List<String> photoList;

    private String urlFormat;

    public FlickrPhotoListCreator(final String apiKey, final String urlFormat, final ObjectMapper objectMapper)
            throws IOException {
        this.urlFormat = String.format(urlFormat, apiKey);
        this.objectMapper = objectMapper;
    }

    public static void main(final String[] args) throws IOException {
        new FlickrPhotoListCreator(args[0], URL_FORMAT, new ObjectMapper()).printList(System.out);
    }

    public void printList(final PrintStream out) throws IOException {
        Collection<Map<String, String>> photos = new ArrayList<>();
        photos.addAll(getPhotoMapsForPage(1));
        photos.addAll(getPhotoMapsForPage(2));
        this.photoList = photos.stream().map(this::buildString).collect(Collectors.toList());
        this.photoList.stream().forEach(out::println);
    }

    private String buildString(final Map<String, String> m) {
        return new StringBuilder(String.format(PAGE, m.get("owner"), m.get("id"))).append(',')
                .append(String.format(THUMBNAIL, m.get("farm"), m.get("server"), m.get("id"), m.get("secret")))
                .toString();
    }

    private Collection<Map<String, String>> getPhotoMapsForPage(final int page) throws IOException {
        InputStream input = new URL(String.format(this.urlFormat, page)).openStream();
        return getPhotosFromMap(this.objectMapper.readValue(input, Map.class));
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    private Collection<Map<String, String>> getPhotosFromMap(final Map result) {
        Map<String, ?> photos = (Map<String, ?>) result.get("photos");
        if (photos == null) {
            throw new LanewebException(result.get("message").toString());
        }
        return (List<Map<String, String>>) photos.get("photo");
    }
}
