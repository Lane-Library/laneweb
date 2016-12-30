package edu.stanford.irt.laneweb.config;

import java.io.IOException;

import javax.sql.DataSource;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.xml.sax.XMLReader;

import edu.stanford.irt.laneweb.equipment.EquipmentStatusTransformer;
import edu.stanford.irt.laneweb.voyager.VoyagerRecordGenerator;

@Configuration
public class EquipmentConfiguration {

    private BeanFactory beanFactory;

    private DataSource dataSource;

    @Autowired
    public EquipmentConfiguration(@Qualifier("javax.sql.DataSource/grandrounds") final DataSource dataSource,
            final BeanFactory beanFactory) {
        this.dataSource = dataSource;
        this.beanFactory = beanFactory;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/equipment")
    @Scope("prototype")
    public VoyagerRecordGenerator equipmentGenerator() throws BeansException, IOException {
        return new VoyagerRecordGenerator(this.dataSource,
                getClass().getResourceAsStream("/edu/stanford/irt/laneweb/equipment/getEquipment.fnc"), 1,
                this.beanFactory.getBean("org.xml.sax.XMLReader/marc", XMLReader.class));
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Transformer/equipment-status")
    @Scope("prototype")
    public EquipmentStatusTransformer equipmentStatusTransformer() {
        return new EquipmentStatusTransformer(this.dataSource);
    }
}
