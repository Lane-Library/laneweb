package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.grandrounds.GrandRoundsException;
import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;
import edu.stanford.lane.catalog.VoyagerInputStream2;

public class GrandRoundsManager {

    private static final Logger LOG = LoggerFactory.getLogger(GrandRoundsManager.class);

    private DataSource dataSource;

    private Map<String, String> departmentMap;

    private String presentationsSQL;

    public GrandRoundsManager(final DataSource dataSource, final InputStream presentationsSQL) throws IOException {
        this.dataSource = dataSource;
        this.presentationsSQL = IOUtils.toString(presentationsSQL, StandardCharsets.UTF_8);
        this.departmentMap = new HashMap<>();
        this.departmentMap.put("medicine", "MEDICINE");
        this.departmentMap.put("emergency", "EMERGENCY MEDICINE");
        this.departmentMap.put("pediatric", "PEDIATRIC");
        this.departmentMap.put("otolaryngology", "OTOLARYNGOLOGY");
    }

    public List<Presentation> getGrandRounds(final String department, final String year) {
        List<Presentation> presentations = new ArrayList<>();
        try (InputStream input = new VoyagerInputStream2(this.dataSource, this.presentationsSQL, 3,
                this.departmentMap.get(department), year)) {
            RecordCollection collection = new RecordCollection(input);
            for (Record record : collection) {
                addPresentationIfValid(new Presentation(record), presentations);
            }
            return presentations.stream().sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                    .collect(Collectors.toList());
        } catch (CatalogSQLException | IOException e) {
            throw new LanewebException(e);
        }
    }

    private void addPresentationIfValid(final Presentation presentation, final List<Presentation> presentations) {
        int recordId = presentation.getId();
        try {
            presentation.getDate();
            presentation.getLinks().stream().forEach(l -> l.getURI());
            presentations.add(presentation);
        } catch (GrandRoundsException e) {
            LOG.error(recordId + " not valid", e);
        }
    }
}
