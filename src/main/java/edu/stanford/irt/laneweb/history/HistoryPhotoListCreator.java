package edu.stanford.irt.laneweb.history;

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

public class HistoryPhotoListCreator {

    private static final String BASE_URL = "https://exhibits.stanford.edu/medhistory/catalog.json?f%5Bformat_main_ssim%5D%5B%5D=Image";

    private static final String PAGE = "https://purl.stanford.edu/%s";

    private static final String THUMBNAIL = PAGE + ".jpg";

    public static void main(final String[] args) {
        try {
            new HistoryPhotoListCreator(BASE_URL).printList(System.out);
        } catch (IOException | NullPointerException e) {
            LoggerFactory.getLogger(HistoryPhotoListCreator.class).error(e.getMessage(), e);
            System.exit(1);
        }
    }

    private ObjectMapper objectMapper;

    String baseUrl;

    public HistoryPhotoListCreator(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = new ObjectMapper();
    }

    public void printList(final PrintStream out) throws IOException {
        Collection<Map<String, ?>> photos = new ArrayList<>();
        String next = this.baseUrl;
        while (null != next) {
            InputStream input = new URL(next).openStream();
            Map<String, ?> map = this.objectMapper.readValue(input, Map.class);
            photos.addAll(getPhotosFromMap(map));
            next = (String) ((Map<String, ?>) map.get("links")).get("next");
        }
        List<String> photoList = photos.stream().map(this::buildString).collect(Collectors.toList());
        photoList.stream().forEach(out::println);
    }

    private String buildString(final Map<String, ?> m) {
        Map<String, ?> attributes = (Map<String, ?>) m.get("attributes");
        String title = ((String) attributes.get("title")).replace("\n", " ");
        return new StringBuilder(String.format(PAGE, m.get("id"))).append('\t')
                .append(String.format(THUMBNAIL, m.get("id"))).append('\t').append(title).toString();
    }

    private Collection<Map<String, ?>> getPhotosFromMap(final Map result) {
        return (List<Map<String, ?>>) result.get("data");
    }
}
