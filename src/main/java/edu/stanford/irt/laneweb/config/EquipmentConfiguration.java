package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import com.fasterxml.jackson.databind.ObjectMapper;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.laneweb.catalog.CatalogRecordGenerator;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentService;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentStatusTransformer;
import edu.stanford.irt.laneweb.catalog.equipment.HTTPEquipmentService;

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
    public EquipmentService httpEquipmentService(final ObjectMapper objectMapper,
            @Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI) {
        return new HTTPEquipmentService(objectMapper, catalogServiceURI);
    }
}
