package edu.stanford.irt.laneweb.spam;

import java.util.Map;

import edu.stanford.irt.status.ApplicationStatus;
import edu.stanford.irt.status.StatusService;

public interface SpamService extends StatusService{

  public boolean isSpam(Spam spam);
  public boolean isSpam(String email, Map<String,Object> identifiers);
  public ApplicationStatus getStatus();

}
