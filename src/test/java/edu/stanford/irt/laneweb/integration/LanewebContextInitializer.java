package edu.stanford.irt.laneweb.integration;

import java.io.File;
import java.io.FileInputStream;
import java.io.FilenameFilter;
import java.io.IOException;

import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.mock.web.MockServletContext;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.LanewebException;

public class LanewebContextInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    @Override
    public void initialize(final ConfigurableApplicationContext applicationContext) {
        File parentDirectory = new File(System.getProperty("user.dir")).getParentFile();
        File[] files = parentDirectory.listFiles(new FilenameFilter() {

            @Override
            public boolean accept(final File dir, final String name) {
                if ("laneweb-test.properties".equals(name)) {
                    return true;
                }
                return false;
            }
        });
        if (files.length != 1) {
            throw new LanewebException(
                    "The integration tests require a laneweb-test.properties file in the project parent directory");
        }
        try {
            System.getProperties().load(new FileInputStream(files[0]));
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        MockServletContext servletContext = (MockServletContext) ((WebApplicationContext) applicationContext)
                .getServletContext();
        servletContext.setInitParameter("laneweb.context.version", "version");
    }
}