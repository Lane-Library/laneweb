package edu.stanford.irt.laneweb.catalog.grandrounds;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import javax.sql.DataSource;

import edu.stanford.lane.catalog.VoyagerInputStream2;

public class JDBCGrandRoundsService extends AbstractGrandRoundsService {

    private DataSource dataSource;

    private Map<String, String> departmentMap;

    private String presentationsSQL;

    public JDBCGrandRoundsService(final DataSource dataSource, final String presentationsSQL) {
        this.dataSource = dataSource;
        this.presentationsSQL = presentationsSQL;
        this.departmentMap = new HashMap<>();
        this.departmentMap.put("medicine", "MEDICINE");
        this.departmentMap.put("emergency", "EMERGENCY MEDICINE");
        this.departmentMap.put("pediatric", "PEDIATRIC");
        this.departmentMap.put("otolaryngology", "OTOLARYNGOLOGY");
    }

    @Override
    protected InputStream getInputStream(final String department, final String year) {
        return new VoyagerInputStream2(this.dataSource, this.presentationsSQL, 3, this.departmentMap.get(department),
                year);
    }
}
