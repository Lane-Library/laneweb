package edu.stanford.irt.laneweb.bookcovers;

import java.net.URI;
import java.util.Collection;
import java.util.Map;
import java.util.stream.Collectors;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.laneweb.rest.TypeReference;
import edu.stanford.irt.status.ApplicationStatus;

public class RESTBookCoverService implements BookCoverService {

    private static final TypeReference<Map<String, String>> TYPE = new TypeReference<Map<String, String>>() {
    };

    private URI bookCoverServiceURI;

    private RESTService restService;

    public RESTBookCoverService(final URI bookCoverServiceURI, final RESTService restService) {
        this.bookCoverServiceURI = bookCoverServiceURI;
        this.restService = restService;
    }

    @Override
    public Map<String, String> getBookCoverURLs(final Collection<String> bcids) {
        StringBuilder queryStringBuilder = new StringBuilder("4/bookcovers?resourceIds=")
                .append(bcids.stream().map(Object::toString).collect(Collectors.joining(",")));
        URI uri = this.bookCoverServiceURI.resolve(queryStringBuilder.toString());
        return this.restService.getObject(uri, TYPE);
    }

    @Override
    public ApplicationStatus getStatus() {
        URI uri = this.bookCoverServiceURI.resolve("status.json");
        
        System.out.println(uri);
        
        return this.restService.getObject(uri, ApplicationStatus.class);
    }
}
