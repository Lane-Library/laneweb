package edu.stanford.irt.laneweb.bookcovers;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.IOUtils;

public class BookCoverService {

    private URI bookCoverServiceURI;

    private ObjectMapper objectMapper;

    public BookCoverService(final ObjectMapper objectMapper, final URI bookCoverServiceURI) {
        this.objectMapper = objectMapper;
        this.bookCoverServiceURI = bookCoverServiceURI;
    }

    public Map<Integer, String> getBookCoverURLs(final List<Integer> bibids) {
        StringBuilder queryStringBuilder = new StringBuilder("bookcovers?bibIDs=")
            .append(bibids.stream().map(Object::toString).collect(Collectors.joining(",")));
        try (InputStream input = IOUtils.getStream(this.bookCoverServiceURI.resolve(queryStringBuilder.toString()))) {
            return this.objectMapper.readValue(input, new TypeReference<Map<Integer, String>>() {
            });
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
