package edu.stanford.irt.laneweb.bookcovers;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;

import com.google.api.client.googleapis.batch.BatchCallback;
import com.google.api.client.googleapis.batch.BatchRequest;
import com.google.api.client.googleapis.json.GoogleJsonErrorContainer;
import com.google.api.client.http.GenericUrl;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestFactory;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.util.ObjectParser;
import com.google.api.services.books.model.Volumes;

import edu.stanford.irt.laneweb.LanewebException;

public class GoogleBookCoverService implements BookCoverService {

    private static final String BASE_URL = "https://www.googleapis.com/books/v1/volumes?key=%s&q=isbn:%%s";

    private String baseURL;

    private Map<Integer, Optional<String>> cache;

    private HttpTransport httpTransport;

    private ISBNService isbnService;

    private ObjectParser objectParser;

    private HttpRequestFactory requestFactory;

    public GoogleBookCoverService(final ISBNService isbnService, final HttpTransport httpTransport,
            final ObjectParser objectParser, final String apiKey, final Map<Integer, Optional<String>> cache) {
        this.isbnService = isbnService;
        this.httpTransport = httpTransport;
        this.objectParser = objectParser;
        this.baseURL = String.format(BASE_URL, apiKey);
        this.requestFactory = httpTransport.createRequestFactory();
        this.cache = cache;
    }

    @Override
    public Map<Integer, String> getBookCoverURLs(final List<Integer> bibids) {
        Map<Integer, String> thumbnailMap;
        if (!bibids.isEmpty()) {
            thumbnailMap = new HashMap<>();
            Map<Integer, List<String>> isbnMap = this.isbnService.getISBNs(bibids);
            BatchRequest batch = createBatchRequest(isbnMap, thumbnailMap);
            if (batch.size() > 0) {
                try {
                    batch.execute();
                } catch (IOException e) {
                    throw new LanewebException(e);
                }
            }
            bibids.stream().filter(i -> !thumbnailMap.containsKey(i)).forEach(i -> thumbnailMap.put(i, null));
            thumbnailMap.entrySet().stream()
                    .forEach(e -> this.cache.put(e.getKey(), Optional.ofNullable(e.getValue())));
        } else {
            thumbnailMap = Collections.emptyMap();
        }
        return thumbnailMap;
    }

    private BatchRequest createBatchRequest(final Map<Integer, List<String>> isbnMap,
            final Map<Integer, String> thumbnailMap) {
        BatchRequest batch = new BatchRequest(this.httpTransport, null);
        for (Entry<Integer, List<String>> entry : isbnMap.entrySet()) {
            Integer bibid = entry.getKey();
            for (String isbn : entry.getValue()) {
                try {
                    HttpRequest request = this.requestFactory.buildGetRequest(new GenericUrl(String.format(this.baseURL, isbn)));
                    request.setParser(this.objectParser);
                    BatchCallback<Volumes, GoogleJsonErrorContainer> callback = new VolumesCallback(bibid,
                            thumbnailMap);
                    batch.queue(request, Volumes.class, GoogleJsonErrorContainer.class, callback);
                } catch (IOException e) {
                    throw new LanewebException(e);
                }
            }
        }
        return batch;
    }
}
