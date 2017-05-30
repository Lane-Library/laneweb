package edu.stanford.irt.laneweb.hours;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.isA;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.same;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.time.LocalDate;
import java.time.Period;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.libraryhours.Hours;
import edu.stanford.irt.libraryhours.LibraryHoursService;

public class HoursGeneratorTest {

    private HoursGenerator generator;

    private List<Hours> hours;

    private SAXStrategy<List<List<Hours>>> saxStrategy;

    private LibraryHoursService service;

    private XMLConsumer xmlConsumer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        this.service = createMock(LibraryHoursService.class);
        this.saxStrategy = createMock(SAXStrategy.class);
        this.generator = new HoursGenerator(this.service, this.saxStrategy);
        this.xmlConsumer = createMock(XMLConsumer.class);
        this.hours = new ArrayList<>(Collections.nCopies(14, null));
    }

    @SuppressWarnings({ "unchecked" })
    @Test
    public void testDoGenerateXMLConsumer() {
        expect(this.service.getHours(LocalDate.parse("2017-02-27"), Period.ofWeeks(4))).andReturn(this.hours);
        this.saxStrategy.toSAX(isA(List.class), same(this.xmlConsumer));
        replay(this.service, this.saxStrategy, this.xmlConsumer);
        this.generator.setModel(Collections.singletonMap(Model.TODAY, LocalDate.parse("2017-03-01")));
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service, this.saxStrategy, this.xmlConsumer);
    }

    @Test
    public void testGetKey() {
        LocalDate date = LocalDate.parse("2017-03-01");
        this.generator.setModel(Collections.singletonMap(Model.TODAY, date));
        assertSame(date, this.generator.getKey());
    }

    @Test
    public void testGetValidity() {
        assertTrue(this.generator.getValidity().isValid());
    }
}
