package edu.stanford.irt.laneweb.util;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class ServiceURIResolver {

    public InputStream getInputStream(final URI uri) throws IOException {
        InputStream inputStream;
        URL url = uri.toURL();
        URLConnection connection = url.openConnection();
        if (HttpURLConnection.class.isAssignableFrom(connection.getClass())) {
            HttpURLConnection httpConnection = HttpURLConnection.class.cast(connection);
            httpConnection.setRequestProperty("Accept-Encoding", "gzip");
            String userInfo = url.getUserInfo();
            if (userInfo != null) {
                String authorization = new StringBuilder("Basic ")
                        .append(Base64.getEncoder().encodeToString(userInfo.getBytes(StandardCharsets.UTF_8)))
                        .toString();
                httpConnection.setRequestProperty("Authorization", authorization);
            }
            inputStream = httpConnection.getInputStream();
            if ("gzip".equals(httpConnection.getContentEncoding())) {
                inputStream = new GZIPInputStream(inputStream);
            }
        } else {
            inputStream = connection.getInputStream();
        }
        return inputStream;
    }
}
