package edu.stanford.irt.laneweb.coursereserves;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public abstract class AbstractCourseReservesGenerator extends AbstractGenerator {

    private DataSource dataSource;

    private SAXStrategy<ResultSet> saxStrategy;

    private String sql;

    public AbstractCourseReservesGenerator(final InputStream sql, final DataSource dataSource,
            final SAXStrategy<ResultSet> saxStrategy) {
        this.dataSource = dataSource;
        this.saxStrategy = saxStrategy;
        try {
            this.sql = IOUtils.toString(sql);
        } catch (IOException e) {
            throw new LanewebException(e);
        }
    }

    protected abstract void doGenerate(Connection conn, String sql, SAXStrategy<ResultSet> saxStrategy,
            XMLConsumer xmlConsumer) throws SQLException;

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try (Connection conn = this.dataSource.getConnection()) {
            doGenerate(conn, this.sql, this.saxStrategy, xmlConsumer);
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
