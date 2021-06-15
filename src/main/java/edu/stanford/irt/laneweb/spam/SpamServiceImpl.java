package edu.stanford.irt.laneweb.spam;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.rest.RESTService;

public class SpamServiceImpl implements SpamService {

  private URI spamURI;

  private RESTService restService;

  private static final Logger log = LoggerFactory.getLogger(SpamServiceImpl.class);

  public SpamServiceImpl(URI spamURI, RESTService restService) {
    this.spamURI = spamURI;
    this.restService = restService;
  }

  @Override
  public boolean isSpam(String portal, String email) {

    boolean isSpam = false;
    try {
      isSpam = this.restService.getObject(this.spamURI.resolve(portal + "/" + email), Boolean.class);
      if (isSpam) {
        log.error("SPAM detected: {}", email);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    log.info("is {} a spam for portal {}? {}", email, portal, isSpam);
    return isSpam;
  }

}
