package edu.stanford.irt.laneweb.spam;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.expectLastCall;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.rest.RESTException;
import edu.stanford.irt.laneweb.rest.RESTService;

public class SpamServiceTest {

  private SpamServiceImpl service;
  private RESTService restService;

  private Map<String, Object> identifiers;

  private URI uri;

  private String email = "alain@stanford.edu";

  private String ip = "171.0.0.0";

  private String portal = "sfp";

  Spam spam;

  @BeforeEach
  public void setUp() throws Exception {
    this.uri = new URI("http://spam.api/");
    this.restService = mock(RESTService.class);
    this.service = new SpamServiceImpl(uri, this.restService);
    this.identifiers = new HashMap<String, Object>();
    this.identifiers.put("remote-addr", ip);
    this.identifiers.put("email", email);
    spam = new Spam(portal, "email", "ip");
  }

  @Test
  public void testIsNotSpam() {
    expect(this.restService.postObject(this.uri.resolve("detection"), spam, Boolean.class)).andReturn(false);
    replay(this.restService);
    assertFalse(service.isSpam(spam));

  }

  @Test
  public void testIsSpam() {
    expect(this.restService.postObject(this.uri.resolve("detection"), spam, Boolean.class)).andReturn(true);
    replay(this.restService);
    assertTrue(service.isSpam(spam));
    verify(this.restService);
  }

  @Test
  public void testExceptionFromSpam() {
    expect(this.restService.postObject(this.uri.resolve("detection"), spam, Boolean.class));
    expectLastCall().andThrow(new RESTException(new IOException()));
    replay(this.restService);
    assertFalse(service.isSpam(spam));
  }

}
