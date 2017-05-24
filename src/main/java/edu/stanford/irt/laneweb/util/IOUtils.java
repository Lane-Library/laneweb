package edu.stanford.irt.laneweb.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;

import edu.stanford.irt.laneweb.LanewebException;

public class IOUtils {

    private IOUtils() {
        // private empty constructor
    }

    public static String getResourceAsString(final Class<?> clazz, final String name) {
        StringBuilder sb = new StringBuilder();
        try (InputStream input = clazz.getResourceAsStream(name);
                InputStreamReader reader = new InputStreamReader(input, StandardCharsets.UTF_8);
                BufferedReader b = new BufferedReader(reader)) {
            String s;
            while ((s = b.readLine()) != null) {
                sb.append(s).append('\n');
            }
        } catch (IOException e) {
            throw new LanewebException(e);
        }
        return sb.toString();
    }
}
