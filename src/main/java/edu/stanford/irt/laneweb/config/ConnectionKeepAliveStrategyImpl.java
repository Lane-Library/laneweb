package edu.stanford.irt.laneweb.config;

import org.apache.http.HeaderElement;
import org.apache.http.HeaderElementIterator;
import org.apache.http.HttpResponse;
import org.apache.http.conn.ConnectionKeepAliveStrategy;
import org.apache.http.message.BasicHeaderElementIterator;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

class ConnectionKeepAliveStrategyImpl implements ConnectionKeepAliveStrategy {

    private static final Logger log = LoggerFactory.getLogger(ConnectionKeepAliveStrategyImpl.class);

    private static final int ONE_SECOND = 1000;

    private static final int TWO_SECONDS = 2000;

    @Override
    public long getKeepAliveDuration(final HttpResponse response, final HttpContext context) {
        HeaderElementIterator it = new BasicHeaderElementIterator(response.headerIterator(HTTP.CONN_KEEP_ALIVE));
        while (it.hasNext()) {
            HeaderElement he = it.nextElement();
            String param = he.getName();
            String value = he.getValue();
            if (value != null && "timeout".equalsIgnoreCase(param)) {
                try {
                    return Long.parseLong(value) * ONE_SECOND;
                } catch (NumberFormatException e) {
                    log.error("failed to parse keepalive timeout value: {}", value);
                }
            }
        }
        // otherwise keep alive for 2 seconds
        return TWO_SECONDS;
    }
}
