package edu.stanford.irt.laneweb.catalog;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;

public class CatalogRecordGenerator extends AbstractGenerator {

    private static final String[] NO_PARAMS = new String[0];

    private CatalogRecordService service;

    private XMLReader xmlReader;

    public CatalogRecordGenerator(final CatalogRecordService service, final XMLReader xmlReader) {
        this.service = service;
        this.xmlReader = xmlReader;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try (InputStream input = this.service.getRecords(Arrays.asList(getParams()))) {
            this.xmlReader.setContentHandler(xmlConsumer);
            this.xmlReader.parse(new InputSource(input));
        } catch (IOException | SAXException e) {
            throw new LanewebException(e);
        }
    }

    protected String[] getParams() {
        return NO_PARAMS;
    }
}
