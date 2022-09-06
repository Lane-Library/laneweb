package edu.stanford.irt.laneweb.catalog.coursereserves;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.util.Collections;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.coursereserves.CourseReservesItemList;
import edu.stanford.irt.laneweb.model.Model;

public class CourseReservesItemListGeneratorTest {

    private CourseReservesItemListGenerator generator;

    private CourseReservesItemList items;

    private SAXStrategy<CourseReservesItemList> saxStrategy;

    private CourseReservesService service;

    private XMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.service = mock(CourseReservesService.class);
        this.saxStrategy = mock(SAXStrategy.class);
        this.generator = new CourseReservesItemListGenerator(this.service, this.saxStrategy);
        this.items = mock(CourseReservesItemList.class);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() {
        expect(this.service.getItems()).andReturn(this.items);
        this.saxStrategy.toSAX(this.items, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.items, this.xmlConsumer);
        this.generator.setModel(Collections.emptyMap());
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.items, this.xmlConsumer);
    }

    @Test
    public void testDoGenerateId() {
        expect(this.service.getItems("1")).andReturn(this.items);
        this.saxStrategy.toSAX(this.items, this.xmlConsumer);
        replay(this.service, this.saxStrategy, this.items, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.ID, "1"));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.items, this.xmlConsumer);
    }
}
