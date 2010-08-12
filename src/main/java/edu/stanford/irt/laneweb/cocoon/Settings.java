package edu.stanford.irt.laneweb.cocoon;

import java.io.File;

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

    public void setTempDir(final File tempDir) {
        super.setCacheDirectory(tempDir.getAbsolutePath());
        super.setWorkDirectory(tempDir.getAbsolutePath());
    }
}
