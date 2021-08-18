package edu.stanford.irt.laneweb.servlet.mvc;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.Assert.assertSame;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.springframework.ui.Model;

import edu.stanford.irt.laneweb.email.EMailSender;
import edu.stanford.irt.laneweb.spam.SpamService;

public class LaneCrmControllerTest {

  private LaneCrmController controller;

  private EMailSender emailSender;
  
  private SpamService spamService;
 
  private Model model;

  Map<String, Object> emailContent;

  private static final String NEXT_PAGE = "redirect:/contacts/sfp-confirmation.html";

  private static final String ERROR_URL = "redirect:/error.html";

  @Before
  public void setUp() {
    this.emailSender = mock(EMailSender.class);
    this.model = mock(Model.class);
    this.spamService = mock(SpamService.class);
    this.controller = new LaneCrmController(this.emailSender, "email@address.com", this.spamService);
    emailContent = new HashMap<String, Object>();
    emailContent.put("title", "title");
  }

  @Test
  public void testSendEmail() throws IOException {
    emailContent.put("requestedBy.email", "test@stanford.edu");
    expect(this.model.asMap()).andReturn(this.emailContent);
    expect(this.spamService.isSpam("sfp",this.emailContent)).andReturn(false);
    this.emailSender.sendEmail(emailContent);
    replay(this.model, this.emailSender, this.spamService);
    String nextPage = this.controller.sendEmail(this.model, null);
    assertSame(NEXT_PAGE, nextPage);
    verify(this.emailSender, this.model, this.spamService);
  }

  @Test
  public void testShcValidEmail() throws IOException {
    emailContent.put("requestedBy.email", "test@stanfordhealthcare.org");
    expect(this.model.asMap()).andReturn(this.emailContent);
    expect(this.spamService.isSpam("sfp",this.emailContent)).andReturn(false);
    this.emailSender.sendEmail(emailContent);
    replay(this.model, this.emailSender, this.spamService);
    String nextPage = this.controller.sendEmail(this.model, null);
    assertSame(NEXT_PAGE, nextPage);
    verify(this.emailSender, this.model, this.spamService);
  }

  @Test
  public void testLpchValidEmail() throws IOException {
    this.emailContent.put("requestedBy.email", "test@stanfordchildrens.org");
    expect(this.model.asMap()).andReturn(this.emailContent);
    expect(this.spamService.isSpam("sfp",this.emailContent)).andReturn(false);
    
    this.emailSender.sendEmail(emailContent);
    replay(this.model, this.emailSender, this.spamService);
    String nextPage = this.controller.sendEmail(this.model, null);
    assertSame(NEXT_PAGE, nextPage);
    verify(this.emailSender, this.model, this.spamService);
  }

  @Test
  public void testNotValidEmail() throws IOException {
    this.emailContent.put("requestedBy.email", "test@stanfordchildens.org");
    expect(this.model.asMap()).andReturn(this.emailContent);
    expect(this.spamService.isSpam("sfp",this.emailContent)).andReturn(false);
    replay(this.model, this.spamService);
    String nextPage = this.controller.sendEmail(this.model, null);
    assertSame(ERROR_URL, nextPage);
    verify(this.model, this.spamService);
  }

  @Test
  public void testSpamEmail() throws IOException {
    emailContent.put("requestedBy.email", "test@stanford.edu");
    expect(this.model.asMap()).andReturn(this.emailContent);
    expect(this.spamService.isSpam("sfp",this.emailContent)).andReturn(true);
    this.emailSender.sendEmail(emailContent);
    replay(this.model, this.emailSender, this.spamService);
    String nextPage = this.controller.sendEmail(this.model, null);
    assertSame(ERROR_URL, nextPage);
    verify(this.model, this.spamService);
  }
  
}
