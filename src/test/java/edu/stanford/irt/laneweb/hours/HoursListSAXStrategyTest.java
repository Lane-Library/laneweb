package edu.stanford.irt.laneweb.hours;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.hamcrest.text.IsEqualCompressingWhiteSpace.equalToCompressingWhiteSpace;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.xml.sax.SAXException;

import edu.stanford.irt.laneweb.TestXMLConsumer;
import edu.stanford.irt.libraryhours.Hours;

public class HoursListSAXStrategyTest {

    private HoursListSAXStrategy generator;

    private Hours hours;

    private TestXMLConsumer xmlConsumer;

    @Before
    public void setUp() {
        this.generator = new HoursListSAXStrategy();
        this.xmlConsumer = new TestXMLConsumer();
        this.hours = mock(Hours.class);
    }

    @Test
    public void testDoGenerateXMLConsumer() throws IOException, SAXException {
        List<Hours> hoursList = new ArrayList<>();
        for (int i = 0; i < 7; i++) {
            hoursList.add(this.hours);
        }
        expect(this.hours.isClosed()).andReturn(false).times(7);
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-02-27"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-02-27T08:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-02-27T22:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-02-28"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-02-28T08:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-02-28T22:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-03-01"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-03-01T08:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-03-01T22:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-03-02"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-03-02T08:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-03-02T22:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-03-03"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-03-03T08:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-03-03T20:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-03-04"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-03-04T10:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-03-04T20:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getDate()).andReturn(LocalDate.parse("2017-03-05"));
        expect(this.hours.getOpen())
                .andReturn(ZonedDateTime.parse("2017-03-05T12:00:00.000-08:00[America/Los_Angeles]"));
        expect(this.hours.getClose())
                .andReturn(ZonedDateTime.parse("2017-03-05T22:00:00.000-08:00[America/Los_Angeles]"));
        replay(this.hours);
        this.xmlConsumer.startDocument();
        this.generator.toSAX(Collections.singletonList(hoursList), this.xmlConsumer);
        this.xmlConsumer.endDocument();
        verify(this.hours);
        assertTrue(equalToCompressingWhiteSpace(
                this.xmlConsumer.getExpectedResult(this, "HoursListSAXStrategyTest-doGenerate.xml"))
                        .matches(this.xmlConsumer.getStringValue()));
    }
}
