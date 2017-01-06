package edu.stanford.irt.laneweb.catalog.coursereserves;

import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.Course;
import edu.stanford.irt.coursereserves.CourseReservesItem;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.resource.AbstractXHTMLSAXStrategy;

public class CourseReservesItemListSAXStrategy extends AbstractXHTMLSAXStrategy<CourseReservesItemList> {

    public SAXStrategy<CourseReservesItem> itemStrategy;

    private SAXStrategy<Course> courseStrategy;

    public CourseReservesItemListSAXStrategy(final SAXStrategy<Course> courseStrategy,
            final SAXStrategy<CourseReservesItem> itemStrategy) {
        this.courseStrategy = courseStrategy;
        this.itemStrategy = itemStrategy;
    }

    @Override
    public void toSAX(final CourseReservesItemList itemList, final XMLConsumer xmlConsumer) {
        try {
            startHTMLDocument(xmlConsumer);
            startBody(xmlConsumer);
            this.courseStrategy.toSAX(itemList.getCourse(), xmlConsumer);
            startUl(xmlConsumer);
            itemList.getItems().stream().forEach(i -> this.itemStrategy.toSAX(i, xmlConsumer));
            endUl(xmlConsumer);
            endBody(xmlConsumer);
            endHTMLDocument(xmlConsumer);
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }
}
