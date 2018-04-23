package edu.stanford.irt.laneweb.bookcovers;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTBookCoverService implements BookCoverService {

    private static final TypeReference<Map<Integer, String>> TYPE = new TypeReference<Map<Integer, String>>() {
    };

    public RESTService restService;

    private URI bookCoverServiceURI;

    public RESTBookCoverService(final URI bookCoverServiceURI, final RESTService restService) {
        this.bookCoverServiceURI = bookCoverServiceURI;
        this.restService = restService;
    }

    @Override
    public Map<Integer, String> getBookCoverURLs(final Collection<Integer> bibids) {
        StringBuilder queryStringBuilder = new StringBuilder("bookcovers?bibIDs=")
                .append(bibids.stream().map(Object::toString).collect(Collectors.joining(",")));
        URI uri = this.bookCoverServiceURI.resolve(queryStringBuilder.toString());
        return this.restService.getObject(uri, TYPE);
    }

    @Override
    public ApplicationStatus getStatus() {
        URI uri = this.bookCoverServiceURI.resolve("status.json");
        return this.restService.getObject(uri, ApplicationStatus.class);
    }
}
