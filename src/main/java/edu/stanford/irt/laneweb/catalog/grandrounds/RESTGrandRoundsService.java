package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.grandrounds.GrandRoundsException;
import edu.stanford.irt.grandrounds.Link;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;

public class RESTGrandRoundsService implements GrandRoundsService {

    private static final String ENDPOINT_PATH_FORMAT = "grandrounds?department=%s&year=%s";

    private static final Logger log = LoggerFactory.getLogger(GrandRoundsService.class);

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URI catalogServiceURI;

    private RESTService restService;

    public RESTGrandRoundsService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Presentation> getGrandRounds(final String department, final String year) {
        List<Presentation> presentations = new ArrayList<>();
        try (InputStream input = getInputStream(department, year)) {
            RecordCollection collection = new RecordCollection(input);
            for (Record record : collection) {
                addPresentationIfValid(new Presentation(record), presentations);
            }
            return presentations.stream()
                    .sorted((final Presentation p1, final Presentation p2) -> p2.getDate().compareTo(p1.getDate()))
                    .collect(Collectors.toList());
        } catch (CatalogSQLException | IOException e) {
            throw new LanewebException(e);
        }
    }

    private void addPresentationIfValid(final Presentation presentation, final List<Presentation> presentations) {
        int recordId = presentation.getId();
        try {
            presentation.getDate();
            presentation.getLinks().stream().forEach(Link::getURI);
            presentations.add(presentation);
        } catch (GrandRoundsException e) {
            log.error(recordId + " not valid", e);
        }
    }

    private InputStream getInputStream(final String department, final String year) {
        String endpointPath = null;
        try {
            endpointPath = String.format(ENDPOINT_PATH_FORMAT, URLEncoder.encode(department, UTF8),
                    URLEncoder.encode(year, UTF8));
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        return this.restService.getInputStream(this.catalogServiceURI.resolve(endpointPath));
    }
}
