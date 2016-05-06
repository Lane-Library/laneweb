package edu.stanford.irt.laneweb.coursereserves;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;

public class CourseReservesGenerator extends AbstractCourseReservesGenerator {

    public CourseReservesGenerator(final InputStream sql, final DataSource dataSource,
            final SAXStrategy<ResultSet> saxStrategy) {
        super(sql, dataSource, saxStrategy);
    }

    @Override
    protected void doGenerate(final Connection conn, final String sql, final SAXStrategy<ResultSet> saxStrategy,
            final XMLConsumer xmlConsumer) throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            try (ResultSet rs = stmt.executeQuery(sql)) {
                saxStrategy.toSAX(rs, xmlConsumer);
            }
        }
    }
}
