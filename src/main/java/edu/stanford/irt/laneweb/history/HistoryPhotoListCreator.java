package edu.stanford.irt.laneweb.history;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.URI;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;
import edu.stanford.irt.laneweb.LanewebException;

public class HistoryPhotoListCreator {

    private static final String BASE_URL = "https://exhibits.stanford.edu/medhistory/catalog.json?f%5Bformat_main_ssim%5D%5B%5D=Image";

    private static final String PAGE = "https://purl.stanford.edu/%s";

    private static final String THUMBNAIL = PAGE + ".jpg";

    public static void main(final String[] args) {
        String url = args.length == 1 ? args[0] : BASE_URL;
        try {
            new HistoryPhotoListCreator(url).printList(System.out);
        } catch (IOException | NullPointerException e) {
            throw new LanewebException(e);
        }
    }

    private JsonMapper objectMapper;

    String baseUrl;

    public HistoryPhotoListCreator(final String baseUrl) {
        this.baseUrl = baseUrl;
        this.objectMapper = new JsonMapper();
    }

    public void printList(final PrintStream out) throws IOException {
        Collection<Map<String, Object>> photos = new ArrayList<>();
        String next = this.baseUrl;
        while (null != next) {
            try (InputStream input = URI.create(next).toURL().openStream()) {
                Map<String, Object> map = this.objectMapper.readValue(input, new TypeReference<Map<String, Object>>() {
                });
                photos.addAll(getPhotosFromMap(map));
                next = (String) ((Map<String, Object>) map.get("links")).get("next");
            }
        }
        List<String> photoList = photos.stream().map(this::buildString).toList();
        photoList.stream().forEach(out::println);
    }

    private String buildString(final Map<String, Object> m) {
        Map<String, Object> attributes = (Map<String, Object>) m.get("attributes");
        String title = ((String) attributes.get("title")).replace("\n", " ");
        return new StringBuilder(String.format(PAGE, m.get("id"))).append('\t')
                .append(String.format(THUMBNAIL, m.get("id"))).append('\t').append(title).toString();
    }

    @SuppressWarnings("unchecked")
    private Collection<Map<String, Object>> getPhotosFromMap(final Map<String, Object> result) {
        return (List<Map<String, Object>>) result.get("data");
    }
}
