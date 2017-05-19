package edu.stanford.irt.laneweb.config;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.sql.DataSource;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.pipeline.Transformer;
import edu.stanford.irt.laneweb.catalog.CatalogRecordGenerator;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentService;
import edu.stanford.irt.laneweb.catalog.equipment.EquipmentStatusTransformer;
import edu.stanford.irt.laneweb.catalog.equipment.JDBCEquipmentStatusService;

@Configuration
public class EquipmentConfiguration {

    private BeanFactory beanFactory;

    private DataSource dataSource;

    @Autowired
    public EquipmentConfiguration(@Qualifier("javax.sql.DataSource/catalog") final DataSource dataSource,
            final BeanFactory beanFactory) {
        this.dataSource = dataSource;
        this.beanFactory = beanFactory;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/equipment")
    @Scope("prototype")
    public Generator equipmentGenerator() throws IOException {
        return new CatalogRecordGenerator(equipmentService(),
                this.beanFactory.getBean("org.xml.sax.XMLReader/marc", XMLReader.class));
    }

    @Bean
    public EquipmentService equipmentService() throws IOException {
        return new JDBCEquipmentStatusService(this.dataSource,
                IOUtils.toString(
                        getClass().getResourceAsStream("/edu/stanford/irt/laneweb/catalog/equipment/getEquipment.fnc"),
                        StandardCharsets.UTF_8));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/equipment-status")
    @Scope("prototype")
    public Transformer equipmentStatusTransformer() throws IOException {
        return new EquipmentStatusTransformer(equipmentService());
    }
}
