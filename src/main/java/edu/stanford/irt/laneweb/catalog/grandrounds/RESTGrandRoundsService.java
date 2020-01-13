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

    private static final String ENDPOINT_PATH_FORMAT_RECENT = "grandrounds/recent?department=%s&limit=%s";

    private static final String ENDPOINT_PATH_FORMAT_YEAR = "grandrounds?department=%s&year=%s";

    private static final Logger log = LoggerFactory.getLogger(RESTGrandRoundsService.class);

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URI catalogServiceURI;

    private RESTService restService;

    public RESTGrandRoundsService(final URI catalogServiceURI, final RESTService restService) {
        this.catalogServiceURI = catalogServiceURI;
        this.restService = restService;
    }

    @Override
    public List<Presentation> getByYear(final String department, final String year) {
        return inputStreamToPresentationList(getInputStream(department, year, null));
    }

    @Override
    public List<Presentation> getRecent(final String department, final String limit) {
        return inputStreamToPresentationList(getInputStream(department, null, limit));
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

    private InputStream getInputStream(final String department, final String year, final String limit) {
        String endpointPath = null;
        try {
            if (null != year) {
                endpointPath = String.format(ENDPOINT_PATH_FORMAT_YEAR, URLEncoder.encode(department, UTF8),
                        URLEncoder.encode(year, UTF8));
            } else {
                endpointPath = String.format(ENDPOINT_PATH_FORMAT_RECENT, URLEncoder.encode(department, UTF8),
                        URLEncoder.encode(limit, UTF8));
            }
        } catch (UnsupportedEncodingException e) {
            // won't happen
        }
        return this.restService.getInputStream(this.catalogServiceURI.resolve(endpointPath));
    }

    private List<Presentation> inputStreamToPresentationList(final InputStream presentationInputStream) {
        List<Presentation> presentations = new ArrayList<>();
        try (InputStream input = presentationInputStream) {
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
}
