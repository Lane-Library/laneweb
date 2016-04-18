package edu.stanford.irt.laneweb.coursereserves;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class DepartmentsGenerator extends AbstractGenerator {

    private static final String SQL;
    static {
        Properties props = new Properties();
        try {
            props.loadFromXML(DepartmentsGenerator.class.getResourceAsStream("course-reserves-sql.xml"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        SQL = props.getProperty("departments-sql");
    }

    private DataSource dataSource;

    private SAXStrategy<ResultSet> saxStrategy;

    public DepartmentsGenerator(final DataSource dataSource, final SAXStrategy<ResultSet> saxStrategy) {
        this.dataSource = dataSource;
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try (Connection conn = this.dataSource.getConnection()) {
            try (Statement stmt = conn.createStatement()) {
                try (ResultSet rs = stmt.executeQuery(SQL)) {
                    this.saxStrategy.toSAX(rs, xmlConsumer);
                }
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
