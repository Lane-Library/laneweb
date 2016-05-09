package edu.stanford.irt.laneweb.coursereserves;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;
import edu.stanford.irt.laneweb.util.XMLUtils;

public class CourseReservesInstructorResultSetSAXStrategy extends AbstractXHTMLSAXStrategy<ResultSet> {

    @Override
    public void toSAX(final ResultSet rs, final XMLConsumer xmlConsumer) {
        try {
            startHTMLDocument(xmlConsumer);
            startBody(xmlConsumer);
            String lastInstructor = null;
            boolean sameInstructor = false;
            while (rs.next()) {
                String thisInstructor = new StringBuilder(rs.getString(4)).append(' ').append(rs.getString(5))
                        .toString();
                sameInstructor = thisInstructor.equals(lastInstructor);
                if (!sameInstructor) {
                    if (lastInstructor != null) {
                        endUl(xmlConsumer);
                    }
                    createElement(xmlConsumer, "h3", thisInstructor);
                    startUl(xmlConsumer);
                }
                startLi(xmlConsumer);
                createAnchor(xmlConsumer, "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?BBID=" + rs.getString(1),
                        rs.getString(2));
                XMLUtils.data(xmlConsumer,
                        new StringBuilder(thisInstructor).append(", ").append(rs.getString(3)).toString());
                endLi(xmlConsumer);
                lastInstructor = thisInstructor;
            }
            endUl(xmlConsumer);
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException | SQLException e) {
            throw new LanewebException(e);
        }
    }
}
