package edu.stanford.irt.laneweb.integration;

import java.io.FileInputStream;
import java.io.IOException;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.LanewebException;

public class LanewebContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        try {
            System.getProperties().load(new FileInputStream("project.properties.default"));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        MockServletContext servletContext = (MockServletContext) ((WebApplicationContext) applicationContext)
                .getServletContext();
        servletContext.setInitParameter("laneweb.context.version", "version");
    }
}