package edu.stanford.irt.laneweb.voyager;

import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.LanewebException;

public class VoyagerLogin {

    private static final String BASE_URL = "http://lmldb.stanford.edu/cgi-bin/Pwebrecon.cgi?";

    private static final String ERROR_URL = "/voyagerError.html";

    private static final Logger LOG = LoggerFactory.getLogger(VoyagerLogin.class);

    private static final Pattern PID_PATTERN = Pattern.compile("[\\w0-9-_]+");

    private LoginService service;

    public VoyagerLogin(final LoginService service) {
        this.service = service;
    }

    public String getVoyagerURL(final String univId, final String pid, final String queryString) {
        String voyagerURL = ERROR_URL;
        if (pid == null || !PID_PATTERN.matcher(pid).matches()) {
            LOG.error("bad pid: {}", pid);
        } else if (univId == null || univId.length() == 0) {
            LOG.error("bad univId: {}", univId);
        } else {
            // voyager data prepends 0
            String voyagerUnivId = "0" + univId;
            try {
                if (this.service.login(voyagerUnivId, pid)) {
                    voyagerURL = BASE_URL.concat(queryString).concat("&authenticate=Y");
                } else {
                    LOG.error("unable to find univId in voyager: {}", univId);
                }
            } catch (LanewebException e) {
                LOG.error(e.getMessage(), e);
                voyagerURL = ERROR_URL;
            }
        }
        return voyagerURL;
    }
}
