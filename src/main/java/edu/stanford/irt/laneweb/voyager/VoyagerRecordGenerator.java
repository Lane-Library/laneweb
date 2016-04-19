package edu.stanford.irt.laneweb.voyager;

import java.io.IOException;
import java.io.InputStream;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.VoyagerInputStream2;

public class VoyagerRecordGenerator extends AbstractGenerator {

    private static final String[] NO_PARAMS = new String[0];

    private DataSource dataSource;

    private int outputIndex;

    private String sql;

    private XMLReader xmlReader;

    public VoyagerRecordGenerator(final DataSource dataSource, final InputStream sql, final int outputIndex,
            final XMLReader xmlReader) throws IOException {
        this.dataSource = dataSource;
        this.sql = IOUtils.toString(sql);
        this.outputIndex = outputIndex;
        this.xmlReader = xmlReader;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try (InputStream input = new VoyagerInputStream2(this.dataSource, this.sql, this.outputIndex, getParams())) {
            this.xmlReader.setContentHandler(xmlConsumer);
            this.xmlReader.parse(new InputSource(input));
        } catch (CatalogSQLException | IOException | SAXException e) {
            throw new LanewebException(e);
        }
    }

    protected String[] getParams() {
        return NO_PARAMS;
    }
}
