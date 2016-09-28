package edu.stanford.irt.laneweb.audio;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.xml.sax.SAXException;

import edu.stanford.irt.cocoon.pipeline.generate.AbstractGenerator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.LanewebException;
import edu.stanford.irt.laneweb.util.XMLUtils;
import edu.stanford.lane.catalog.CatalogSQLException;
import edu.stanford.lane.catalog.Record;
import edu.stanford.lane.catalog.RecordCollection;
import edu.stanford.lane.catalog.VoyagerInputStream2;

public class AudioGenerator extends AbstractGenerator {

    private SAXStrategy<Record> recordSAXStrategy;

    private DataSource dataSource;

    private String audioSQL;

    public AudioGenerator(final DataSource dataSource, final InputStream audioSQL, final SAXStrategy<Record> recordSAXStrategy) throws IOException {
        this.dataSource = dataSource;
        this.audioSQL = IOUtils.toString(audioSQL, StandardCharsets.UTF_8);
        this.recordSAXStrategy = recordSAXStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        List<Record> records = getRecords();
        try {
            xmlConsumer.startDocument();
            XMLUtils.startElement(xmlConsumer, "http://www.loc.gov/MARC21/slim", "collection");
            records.stream().forEach(r -> this.recordSAXStrategy.toSAX(r, xmlConsumer));
            XMLUtils.endElement(xmlConsumer, "http://www.loc.gov/MARC21/slim", "collection");
            xmlConsumer.endDocument();
        } catch (SAXException e) {
            throw new LanewebException(e);
        }
    }

    private List<Record> getRecords() {
        List<Record> records = new ArrayList<>();
        try (InputStream input = new VoyagerInputStream2(this.dataSource, this.audioSQL, 1)) {
            RecordCollection collection = new RecordCollection(input);
            for (Record record : collection) {
                records.add(record);
            }
        } catch (CatalogSQLException | IOException e) {
            throw new LanewebException(e);
        }
        return records;
    }
}
