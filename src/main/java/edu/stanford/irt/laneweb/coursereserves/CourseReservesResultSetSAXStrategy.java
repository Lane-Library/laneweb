package edu.stanford.irt.laneweb.coursereserves;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesResultSetSAXStrategy extends AbstractXHTMLSAXStrategy<ResultSet> {

    @Override
    public void toSAX(final ResultSet rs, final XMLConsumer xmlConsumer) {
        try {
            startHTMLDocument(xmlConsumer);
            startBody(xmlConsumer);
            startUl(xmlConsumer);
            while (rs.next()) {
                startLi(xmlConsumer);
                createAnchor(xmlConsumer, "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + rs.getString(1),
                        rs.getString(2));
                XMLUtils.data(xmlConsumer, rs.getString(3));
                endLi(xmlConsumer);
            }
            endUl(xmlConsumer);
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException | SQLException e) {
            throw new LanewebException(e);
        }
    }
}
