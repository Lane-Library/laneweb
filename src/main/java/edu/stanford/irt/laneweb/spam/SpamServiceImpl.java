package edu.stanford.irt.laneweb.spam;

import java.net.URI;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import edu.stanford.irt.laneweb.rest.RESTService;
import edu.stanford.irt.status.ApplicationStatus;

public class SpamServiceImpl implements SpamService {

  private URI spamURI;

  private RESTService restService;
  
  private String endPointUrl = "detection";

  private static final Logger log = LoggerFactory.getLogger(SpamServiceImpl.class);

  public SpamServiceImpl(URI spamURI, RESTService restService) {
    this.spamURI = spamURI;
    this.restService = restService;
  }

  public boolean isSpam(String portal, Map<String, Object> identifiers) {
    String ip = (String) identifiers.get("remote-addr");
    String email = (String) identifiers.get("email");
    Spam spam = new Spam(portal,email,ip);
    return this.isSpam(spam);
  }

  public boolean isSpam(Spam spam) {
    boolean isSpam = false;
    try {
      isSpam = this.restService.postObject(this.spamURI.resolve(endPointUrl), spam, Boolean.class);
      if (isSpam) {
        log.warn("SPAM detected:  Email {} and IP {} for portal {}  ", spam.getEmail(), spam.getIp(), spam.getPortal());
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    log.info("is email:{} or IP:{} a spam for portal {}? {}",  spam.getEmail(), spam.getIp(), spam.getPortal(), isSpam);
    return isSpam;
  }

  @Override
  public ApplicationStatus getStatus() {
    URI uri = this.spamURI.resolve("status.json");
    return this.restService.getObject(uri, ApplicationStatus.class);
  }

}
