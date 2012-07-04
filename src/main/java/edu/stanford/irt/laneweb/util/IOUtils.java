package edu.stanford.irt.laneweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public abstract class IOUtils {

    private static final Logger LOG = LoggerFactory.getLogger(IOUtils.class);

    public static void closeStream(final InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                LOG.warn("Could not close InputStream", e);
            }
        }
    }

    public static void closeStream(final OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                LOG.warn("Could not close OutputStream", e);
            }
        }
    }
}
