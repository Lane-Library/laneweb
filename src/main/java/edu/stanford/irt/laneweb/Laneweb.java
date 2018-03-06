package edu.stanford.irt.laneweb;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
@ServletComponentScan
public class Laneweb {

    public static void main(final String[] args) {
        SpringApplication.run(Laneweb.class, args);
    }
    
    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(
            @Value("${edu.stanford.irt.laneweb.ajpPort:8009}") int ajpPort) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(ajpPort);
        tomcat.addAdditionalTomcatConnectors(ajpConnector);
        return tomcat;
    }
}
