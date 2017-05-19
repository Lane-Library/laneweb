package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.coursereserves.CourseReservesService;
import edu.stanford.irt.coursereserves.JDBCCourseReservesService;
import edu.stanford.irt.laneweb.model.Model;

public class CourseReservesItemListGeneratorTest {

    private CourseReservesService dao;

    private CourseReservesItemListGenerator generator;

    private CourseReservesItemList items;

    private SAXStrategy<CourseReservesItemList> saxStrategy;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.dao = createMock(JDBCCourseReservesService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new CourseReservesItemListGenerator(this.dao, this.saxStrategy);
        this.items = createMock(CourseReservesItemList.class);
        this.xmlConsumer = createMock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.dao.getItems()).andReturn(this.items);
        this.saxStrategy.toSAX(this.items, this.xmlConsumer);
        replay(this.dao, this.saxStrategy, this.items, this.xmlConsumer);
        this.generator.setModel(Collections.emptyMap());
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dao, this.saxStrategy, this.items, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateId() {
        expect(this.dao.getItems(1)).andReturn(this.items);
        this.saxStrategy.toSAX(this.items, this.xmlConsumer);
        replay(this.dao, this.saxStrategy, this.items, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.ID, "1"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.dao, this.saxStrategy, this.items, this.xmlConsumer);
    }
}
