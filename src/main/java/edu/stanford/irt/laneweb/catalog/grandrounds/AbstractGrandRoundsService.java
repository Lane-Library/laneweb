package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.grandrounds.GrandRoundsException;
import edu.stanford.irt.grandrounds.Link;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;

public abstract class AbstractGrandRoundsService implements GrandRoundsService {

    private static final Logger LOG = LoggerFactory.getLogger(GrandRoundsService.class);

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

    protected abstract InputStream getInputStream(String department, String year);

    private void addPresentationIfValid(final Presentation presentation, final List<Presentation> presentations) {
        int recordId = presentation.getId();
        try {
            presentation.getDate();
            presentation.getLinks().stream().forEach(Link::getURI);
            presentations.add(presentation);
        } catch (GrandRoundsException e) {
            LOG.error(recordId + " not valid", e);
        }
    }
}
