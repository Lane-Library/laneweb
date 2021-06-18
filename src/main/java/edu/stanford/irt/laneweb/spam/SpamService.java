package edu.stanford.irt.laneweb.spam;

import java.util.Map;

public interface SpamService {

  public boolean isSpam(String portal, String identifier);
  public boolean isSpam(String email, Map<String,Object> identifiers);

}
