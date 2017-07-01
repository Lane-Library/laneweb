package edu.stanford.irt.laneweb.config;

import static edu.stanford.irt.laneweb.util.IOUtils.getResourceAsString;

import java.net.URI;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.laneweb.catalog.CatalogRecordGenerator;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentService;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentStatusTransformer;
import edu.stanford.irt.laneweb.catalog.equipment.HTTPEquipmentService;
import edu.stanford.irt.laneweb.catalog.equipment.JDBCEquipmentService;

@Configuration
public class EquipmentConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/equipment")
    @Scope("prototype")
    public Generator equipmentGenerator(final EquipmentService equipmentService,
            @Qualifier("org.xml.sax.XMLReader/marc") final XMLReader marcXMLReader) {
        return new CatalogRecordGenerator(equipmentService, marcXMLReader);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/equipment-status")
    @Scope("prototype")
    public Transformer equipmentStatusTransformer(final EquipmentService equipmentService) {
        return new EquipmentStatusTransformer(equipmentService);
    }

    @Bean
    @Profile("gce")
    public EquipmentService httpEquipmentService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPEquipmentService(objectMapper, catalogServiceURI);
    }

    @Bean
    @Profile("!gce")
    public EquipmentService jdbcEquipmentService(
            @Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource) {
        return new JDBCEquipmentService(dataSource,
                getResourceAsString(EquipmentService.class, "getEquipment.fnc"),
                getResourceAsString(EquipmentService.class, "getStatus.sql"));
    }
}
