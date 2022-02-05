package edu.stanford.irt.laneweb.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.laneweb.staff.StaffGenerator;
import edu.stanford.irt.laneweb.staff.StaffSAXStrategy;
@Configuration
public class StaffConfiguration {
    
    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/staff")
    @Scope("prototype")
    public Generator staffGenerator() {
        return new StaffGenerator(  new StaffSAXStrategy());
    }

}
