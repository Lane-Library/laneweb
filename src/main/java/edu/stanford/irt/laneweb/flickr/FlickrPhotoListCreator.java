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

import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;

public class FlickrPhotoListCreator {

    private static final String PAGE = "https://www.flickr.com/photos/%s/%s";

    private static final int PAGE_COUNT = 2;

    private static final String THUMBNAIL = "https://farm%s.staticflickr.com/%s/%s_%s_m.jpg";

    private static final String URL_FORMAT = "https://api.flickr.com/services/rest/?method=flickr.people.getPublicPhotos&user_id=40390680@N08&api_key=%s&per_page=500&format=json&nojsoncallback=1&page=%%d";

    public static void main(final String[] args) {
        try {
            new FlickrPhotoListCreator(args[0], URL_FORMAT, new ObjectMapper()).printList(System.out);
        } catch (IOException | NullPointerException e) {
            LoggerFactory.getLogger(FlickrPhotoListCreator.class).error(e.getMessage(), e);
            System.exit(1);
        }
    }

    private ObjectMapper objectMapper;

    private String urlFormat;

    public FlickrPhotoListCreator(final String apiKey, final String urlFormat, final ObjectMapper objectMapper) {
        this.urlFormat = String.format(urlFormat, apiKey);
        this.objectMapper = objectMapper;
    }

    public void printList(final PrintStream out) throws IOException {
        Collection<Map<String, String>> photos = new ArrayList<>();
        for (int page = 1; page <= PAGE_COUNT; page++) {
            photos.addAll(getPhotoMapsForPage(page));
        }
        List<String> photoList = photos.stream().map(this::buildString).collect(Collectors.toList());
        photoList.stream().forEach(out::println);
    }

    private String buildString(final Map<String, String> m) {
        return new StringBuilder(String.format(PAGE, m.get("owner"), m.get("id"))).append('\t')
                .append(String.format(THUMBNAIL, m.get("farm"), m.get("server"), m.get("id"), m.get("secret")))
                .append('\t').append(m.get("title")).toString();
    }

    private Collection<Map<String, String>> getPhotoMapsForPage(final int page) throws IOException {
        InputStream input = new URL(String.format(this.urlFormat, page)).openStream();
        return getPhotosFromMap(this.objectMapper.readValue(input, Map.class));
    }

    private Collection<Map<String, String>> getPhotosFromMap(final Map result) {
        Map<String, ?> photos = (Map<String, ?>) result.get("photos");
        if (photos == null) {
            throw new LanewebException(result.get("message").toString());
        }
        return (List<Map<String, String>>) photos.get("photo");
    }
}
