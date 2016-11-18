package edu.stanford.irt.laneweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import edu.stanford.irt.laneweb.LanewebException;

public class IOUtils {

    private IOUtils() {
        // empty private default constructor
    }

    public static void closeStream(final InputStream inputStream) {
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
    }

    public static void closeStream(final OutputStream outputStream) {
        if (outputStream != null) {
            try {
                outputStream.close();
            } catch (IOException e) {
                throw new LanewebException(e);
            }
        }
    }
}
