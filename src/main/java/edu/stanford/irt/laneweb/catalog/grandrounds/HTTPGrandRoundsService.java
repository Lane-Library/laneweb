package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.IOException;
import java.io.InputStream;
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
import edu.stanford.irt.laneweb.util.ServiceURIResolver;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;

public class HTTPGrandRoundsService implements GrandRoundsService {

    private static final String ENDPOINT_PATH_FORMAT = "grandrounds?department=%s&year=%s";

    private static final Logger log = LoggerFactory.getLogger(GrandRoundsService.class);

    private static final String UTF8 = StandardCharsets.UTF_8.name();

    private URI catalogServiceURI;
    
    private ServiceURIResolver uriResolver;

    public HTTPGrandRoundsService(final URI catalogServiceURI, final ServiceURIResolver uriResolver) {
        this.catalogServiceURI = catalogServiceURI;
        this.uriResolver = uriResolver;
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
                    .sorted((final Presentation p1, final Presentation p2)
                            -> p2.getDate().compareTo(p1.getDate()))
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
        try {
            String endpointPath = String.format(ENDPOINT_PATH_FORMAT, URLEncoder.encode(department, UTF8),
                    URLEncoder.encode(year, UTF8));
            return this.uriResolver.getInputStream(this.catalogServiceURI.resolve(endpointPath));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }
}
