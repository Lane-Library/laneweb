package edu.stanford.irt.laneweb.equipment;

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
import edu.stanford.lane.catalog.impl.xml.DefaultMarcReader;

public class EquipmentGenerator extends AbstractGenerator {

    private DataSource dataSource;

    private String equipmentSQL;

    public EquipmentGenerator(final DataSource dataSource, final InputStream equipmentSQL) throws IOException {
        this.dataSource = dataSource;
        this.equipmentSQL = IOUtils.toString(equipmentSQL);
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        try (InputStream input = new VoyagerInputStream2(this.dataSource, this.equipmentSQL, 1)) {
            XMLReader reader = new DefaultMarcReader();
            reader.setContentHandler(xmlConsumer);
            reader.parse(new InputSource(input));
        } catch (CatalogSQLException | IOException | SAXException e) {
            throw new LanewebException(e);
        }
    }
}
