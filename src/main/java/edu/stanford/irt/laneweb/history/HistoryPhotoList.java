package edu.stanford.irt.laneweb.history;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.function.Function;

public class HistoryPhotoList extends ArrayList<HistoryPhoto> {

    private static final long serialVersionUID = 1L;

    private static Function<String, String[]> split = (final String s) -> s.split("\t");

    public HistoryPhotoList(final InputStream input) throws IOException {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(input, StandardCharsets.UTF_8))) {
            br.lines().map(split).forEach((final String[] s) -> add(new HistoryPhoto(s[0], s[1], s[2])));
        }
    }
}
