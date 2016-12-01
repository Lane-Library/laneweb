package edu.stanford.irt.laneweb.integration;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

public class LanewebContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        MockServletContext servletContext = (MockServletContext) ((WebApplicationContext) applicationContext)
                .getServletContext();
        servletContext.setInitParameter("laneweb.context.version", "version");
    }
}