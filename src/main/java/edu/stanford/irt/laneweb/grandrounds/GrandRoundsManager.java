package edu.stanford.irt.laneweb.grandrounds;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import edu.stanford.irt.grandrounds.Presentation;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;
import edu.stanford.lane.catalog.VoyagerInputStream2;

public class GrandRoundsManager {

    private DataSource dataSource;

    private Map<String, String> departmentMap;

    private String presentationsSQL;

    public GrandRoundsManager(final DataSource dataSource, final InputStream presentationsSQL) throws IOException {
        this.dataSource = dataSource;
        this.presentationsSQL = IOUtils.toString(presentationsSQL);
        this.departmentMap = new HashMap<String, String>();
        this.departmentMap.put("medicine", "MEDICINE");
        this.departmentMap.put("emergency", "EMERGENCY MEDICINE");
        this.departmentMap.put("pediatric", "PEDIATRIC");
        this.departmentMap.put("otolaryngology", "OTOLARYNGOLOGY");
    }

    public List<Presentation> getGrandRounds(final String department, final String year) {
        List<Presentation> presentations = new ArrayList<Presentation>();
        try (InputStream input = new VoyagerInputStream2(this.dataSource, this.presentationsSQL, 3,
                this.departmentMap.get(department), year)) {
            RecordCollection collection = new RecordCollection(input);
            for (Record record : collection) {
                presentations.add(new Presentation(record));
            }
            return presentations.stream().sorted((p1, p2) -> p2.getDate().compareTo(p1.getDate()))
                    .collect(Collectors.toList());
        } catch (CatalogSQLException | IOException e) {
            throw new LanewebException(e);
        }
    }
}
