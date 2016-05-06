package edu.stanford.irt.laneweb.coursereserves;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class DepartmentsResultSetSAXStrategy extends AbstractXHTMLSAXStrategy<ResultSet> {

    @Override
    public void toSAX(final ResultSet rs, final XMLConsumer xmlConsumer) {
        try {
            xmlConsumer.startDocument();
            while (rs.next()) {
                startLi(xmlConsumer);
                createAnchor(xmlConsumer, "/samples/course-reserves.html?id=" + rs.getString(1), rs.getString(2));
                endLi(xmlConsumer);
            }
            xmlConsumer.endDocument();
        } catch (SAXException | SQLException e) {
            throw new LanewebException(e);
        }
    }
}
