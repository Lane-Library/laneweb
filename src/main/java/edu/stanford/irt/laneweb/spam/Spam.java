package edu.stanford.irt.laneweb.spam;

public class Spam {
  
  
  
  public Spam(String portal, String email, String ip) {
    this.portal = portal;
    this.email = email;
    this.ip = ip;
  }

  private String portal;
  
  private String email;

  private String ip;
  
  private String client = "laneweb";
  
  public String getClient() {
    return client;
  }

  public String getPortal() {
    return portal;
  }

  public void setPortal(String portal) {
    this.portal = portal;
  }

  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }

  public String getIp() {
    return ip;
  }

  public void setIp(String ip) {
    this.ip = ip;
  }

}
