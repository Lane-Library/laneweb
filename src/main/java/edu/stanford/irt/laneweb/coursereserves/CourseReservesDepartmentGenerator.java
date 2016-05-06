package edu.stanford.irt.laneweb.coursereserves;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;
import java.util.Objects;

import javax.sql.DataSource;

import edu.stanford.irt.cocoon.pipeline.ModelAware;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.model.ModelUtil;

public class CourseReservesDepartmentGenerator extends AbstractCourseReservesGenerator implements ModelAware {

    private String id;

    public CourseReservesDepartmentGenerator(final InputStream sql, final DataSource dataSource,
            final SAXStrategy<ResultSet> saxStrategy) {
        super(sql, dataSource, saxStrategy);
    }

    @Override
    public void setModel(final Map<String, Object> model) {
        this.id = ModelUtil.getString(model, Model.ID);
        Objects.requireNonNull(this.id, "null department id");
    }

    @Override
    protected void doGenerate(final Connection conn, final String sql, final SAXStrategy<ResultSet> saxStrategy,
            final XMLConsumer xmlConsumer) throws SQLException {
        try (PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, this.id);
            try (ResultSet rs = stmt.executeQuery()) {
                saxStrategy.toSAX(rs, xmlConsumer);
            }
        }
    }
}
