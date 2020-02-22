package edu.stanford.irt.laneweb;

import org.apache.catalina.connector.Connector;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.web.servlet.error.ErrorMvcAutoConfiguration;
import org.springframework.boot.web.embedded.tomcat.TomcatServletWebServerFactory;
import org.springframework.boot.web.servlet.ServletComponentScan;
import org.springframework.boot.web.servlet.server.ConfigurableServletWebServerFactory;
import org.springframework.context.annotation.Bean;

@SpringBootApplication(exclude = { ErrorMvcAutoConfiguration.class })
@ServletComponentScan
public class Laneweb {

    public static void main(final String[] args) {
        SpringApplication.run(Laneweb.class, args);
    }

    @Bean
    public ConfigurableServletWebServerFactory webServerFactory(
            @Value("${edu.stanford.irt.laneweb.ajpPort:8009}") final int ajpPort) {
        TomcatServletWebServerFactory tomcat = new TomcatServletWebServerFactory();
        Connector ajpConnector = new Connector("AJP/1.3");
        ajpConnector.setPort(ajpPort);
        ajpConnector.setProperty("secretRequired", "false");
        ajpConnector.setProperty("address", "0.0.0.0");
        ajpConnector.setProperty("allowedRequestAttributesPattern",
                "(Shib\\-Identity\\-Provider|affiliation|cn|displayName|eduPersonEntitlement|eppn|givenName"
                        + "|group|mail|ou|persistent-id|postalAddress|sn|street|suAffiliation|suDisplayNameLF"
                        + "|suUnivID|targeted\\-id|telephoneNumber|title|uid|uid\\-alt|unscoped\\-affiliation|upn)");
        ajpConnector.setProperty("tomcatAuthentication", "false");
        ajpConnector.setProperty("URIEncoding", "UTF-8");
        tomcat.addAdditionalTomcatConnectors(ajpConnector);
        return tomcat;
    }
}
