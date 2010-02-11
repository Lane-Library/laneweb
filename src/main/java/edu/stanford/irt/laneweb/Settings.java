package edu.stanford.irt.laneweb;

import java.io.File;

import javax.servlet.ServletContext;

import org.apache.cocoon.configuration.MutableSettings;

public class Settings extends MutableSettings {

    public Settings() {
        super("prod");
        super.setConfigurationReloadDelay(-1);
        super.setContainerEncoding("ISO-8859-1");
        super.setCreationTime(System.currentTimeMillis());
        super.setFormEncoding("UTF-8");
        super.setReloadingEnabled(false);
    }

    public void setServletContext(final ServletContext servletContext) {
        File temp = (File) servletContext.getAttribute("javax.servlet.context.tempdir");
        super.setCacheDirectory(temp.getAbsolutePath());
        super.setWorkDirectory(temp.getAbsolutePath());
    }
}
