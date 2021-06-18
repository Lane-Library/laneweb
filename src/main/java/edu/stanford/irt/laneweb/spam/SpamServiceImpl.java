package edu.stanford.irt.laneweb.spam;

import java.net.URI;
import java.util.Map;

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

  public boolean isSpam(String portal, Map<String,Object> identifiers) {
    String ip = (String) identifiers.get("remote-addr");
    if(this.isSpam(portal, ip)) {
      return true;
    }
    String email = (String)identifiers.get("email");
    return this.isSpam(portal, email);
  }

  public boolean isSpam(String portal, String identifier) {
    boolean isSpam = false;
    try {
      isSpam = this.restService.getObject(this.spamURI.resolve(portal + "/" + identifier), Boolean.class);
      if (isSpam) {
        log.error("SPAM detected:  {} for portal {}  ", identifier, portal);
      }
    } catch (Exception e) {
      log.error(e.getMessage(), e);
    }
    log.info("is {} a spam for portal {}? {}", identifier, portal, isSpam);
    return isSpam;
  }



}
