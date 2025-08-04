package edu.stanford.irt.laneweb.config;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.catalog.equipment.Equipment;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentListGenerator;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentListSAXStrategy;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentService;
import edu.stanford.irt.laneweb.catalog.equipment.RESTEquipmentService;
import edu.stanford.irt.laneweb.rest.BasicAuthRESTService;

@Configuration
public class EquipmentConfiguration {

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/equipment-list") @Scope("prototype")
    public Generator equipmentListGenerator(final EquipmentService equipmentService) {
        return new EquipmentListGenerator(equipmentService, equipmentListSAXStrategy());
    }

    @Bean
    public SAXStrategy<List<Equipment>> equipmentListSAXStrategy() {
        return new EquipmentListSAXStrategy();
    }

    @Bean
    public EquipmentService restEquipmentService(@Qualifier("java.net.URI/catalog-service") final URI catalogServiceURI,
            @Qualifier("restService/catalog-service") final BasicAuthRESTService restService) {
        return new RESTEquipmentService(catalogServiceURI, restService);
    }
}
