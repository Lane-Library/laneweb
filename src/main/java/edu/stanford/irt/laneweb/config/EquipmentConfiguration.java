package edu.stanford.irt.laneweb.config;

import java.net.URI;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
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

    private BeanFactory beanFactory;

    private URI catalogServiceURI;

    private ObjectMapper objectMapper;

    @Autowired
    public EquipmentConfiguration(final ObjectMapper objectMapper, final BeanFactory beanFactory,
            final URI catalogServiceURI) {
        this.objectMapper = objectMapper;
        this.beanFactory = beanFactory;
        this.catalogServiceURI = catalogServiceURI;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/equipment")
    @Scope("prototype")
    public Generator equipmentGenerator() {
        return new CatalogRecordGenerator(equipmentService(),
                this.beanFactory.getBean("org.xml.sax.XMLReader/marc", XMLReader.class));
    }

    @Bean
    public EquipmentService equipmentService() {
        return new HTTPEquipmentService(this.objectMapper, this.catalogServiceURI);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/equipment-status")
    @Scope("prototype")
    public Transformer equipmentStatusTransformer() {
        return new EquipmentStatusTransformer(equipmentService());
    }
}
