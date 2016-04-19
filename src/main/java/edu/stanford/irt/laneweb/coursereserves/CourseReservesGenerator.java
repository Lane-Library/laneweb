package edu.stanford.irt.laneweb.coursereserves;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CourseReservesGenerator extends AbstractGenerator implements ModelAware {

    private static final String SQL;
    static {
        Properties props = new Properties();
        try {
            props.loadFromXML(CourseReservesGenerator.class.getResourceAsStream("course-reserves-sql.xml"));
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
        SQL = props.getProperty("course-reserves-sql");
    }

    private DataSource dataSource;

    private String id;

    private SAXStrategy<ResultSet> saxStrategy;

    public CourseReservesGenerator(final DataSource dataSource, final SAXStrategy<ResultSet> saxStrategy) {
        this.dataSource = dataSource;
        this.saxStrategy = saxStrategy;
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.id = ModelUtil.getString(model, Model.ID);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        Objects.requireNonNull(this.id);
        try (Connection conn = this.dataSource.getConnection()) {
            try (PreparedStatement stmt = conn.prepareStatement(SQL)) {
                stmt.setString(1, this.id);
                try (ResultSet rs = stmt.executeQuery()) {
                    this.saxStrategy.toSAX(rs, xmlConsumer);
                }
            }
        } catch (SQLException e) {
            throw new LanewebException(e);
        }
    }
}
