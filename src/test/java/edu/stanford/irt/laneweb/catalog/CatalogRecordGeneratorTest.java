package edu.stanford.irt.laneweb.catalog;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;
import java.util.Collections;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.XMLReaderFactory;

import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class CatalogRecordGeneratorTest {

    private CatalogRecordGenerator generator;

    private CatalogRecordService service;

    private XMLConsumer xmlConsumer;

    private XMLReader xmlReader;

    @BeforeEach
    public void setUp() throws IOException, SAXException {
        this.service = mock(CatalogRecordService.class);
        this.xmlReader = XMLReaderFactory.createXMLReader();
        this.generator = new CatalogRecordGenerator(this.service, this.xmlReader);
        this.xmlConsumer = mock(XMLConsumer.class);
    }

    @Test
    public void testDoGenerate() throws SQLException, IOException, SAXException {
        expect(this.service.getRecords(Collections.emptyList()))
                .andReturn(new ByteArrayInputStream("<foo/>".getBytes()));
        replay(this.service);
        this.generator.doGenerate(this.xmlConsumer);
        verify(this.service);
    }

    @Test
    public void testDoGenerateIOException() throws SQLException, IOException {
        InputStream input = mock(InputStream.class);
        IOException exception = new IOException();
        expect(this.service.getRecords(Collections.emptyList())).andReturn(input);
        expect(input.read()).andThrow(exception);
        input.close();
        replay(this.service, input);
        try {
            this.generator.doGenerate(this.xmlConsumer);
        } catch (LanewebException e) {
            assertSame(exception, e.getCause());
        }
        verify(this.service, input);
    }
}
