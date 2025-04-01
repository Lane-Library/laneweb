package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.mock;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.HashMap;
import java.util.Map;

import jakarta.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SecureHttpParameterValueServletRequestTest {

  private SecureHttpParameterValueServletRequest secureServletRequest;

  private HttpServletRequest request;

  private static final String EXCEPTED_VALUE = "hello";

  private static final String[] EXCEPTED_ARRAY_VALUE = { "hello", "you" };

  private static final String[] FORBIDDEN_ARRAY_VALUE = { "hel\0lo", "\0you" };

  @BeforeEach
  public void setUp() {
    this.request = mock(HttpServletRequest.class);
    this.secureServletRequest = new SecureHttpParameterValueServletRequest(request);
  }

  @Test
  public void testGetParameter() {
    expect(this.request.getParameter("q")).andReturn("hello");
    replay(this.request);
    assertSame(EXCEPTED_VALUE, this.secureServletRequest.getParameter("q"));
    verify(this.request);
  }

  @Test
  public void testGetParameterNullCharacterValue() {
    expect(this.request.getParameter("q")).andReturn("he\0llo");
    replay(this.request);
    assertEquals(EXCEPTED_VALUE, this.secureServletRequest.getParameter("q"));
    verify(this.request);
  }

  @Test
  public void testGetParameterNull() {
    expect(this.request.getParameter("q")).andReturn(null);
    replay(this.request);
    assertSame(null, this.secureServletRequest.getParameter("q"));
    verify(this.request);
  }

  @Test
  public void testGetParameterValues() {
    expect(this.request.getParameterValues("q")).andReturn(EXCEPTED_ARRAY_VALUE);
    replay(this.request);
    String[] expectedValues = this.secureServletRequest.getParameterValues("q");
    assertEquals(EXCEPTED_VALUE, expectedValues[0]);
    assertEquals("you", expectedValues[1]);
    verify(this.request);
  }

  @Test
  public void testGetParameterValuesNullCharacterValue() {
    expect(this.request.getParameterValues("q")).andReturn(FORBIDDEN_ARRAY_VALUE);
    replay(this.request);
    String[] expectedValues = this.secureServletRequest.getParameterValues("q");
    assertEquals(EXCEPTED_VALUE, expectedValues[0]);
    assertEquals("you", expectedValues[1]);
    verify(this.request);
  }

  @Test
  public void testGetParameterValuesNull() {
    expect(this.request.getParameterValues("q")).andReturn(null);
    replay(this.request);
    assertSame(null, this.secureServletRequest.getParameterValues("q"));
    verify(this.request);
  }

  @Test
  public void testGetParameterMap() {
    Map<String, String[]> parameterMap = new HashMap<>();
    parameterMap.put("q", EXCEPTED_ARRAY_VALUE);
    expect(this.request.getParameterMap()).andReturn(parameterMap);
    replay(this.request);
    Map<String, String[]> secureParameterMap = this.secureServletRequest.getParameterMap();
    String[] secureParameter = secureParameterMap.get("q");
    assertEquals(EXCEPTED_VALUE, secureParameter[0]);
    assertEquals("you", secureParameter[1]);
    verify(this.request);
  }

  @Test
  public void testGetParameterMapNullCharacterValue() {
    Map<String, String[]> parameterMap = new HashMap<>();
    parameterMap.put("q", EXCEPTED_ARRAY_VALUE);
    expect(this.request.getParameterMap()).andReturn(parameterMap);
    replay(this.request);
    Map<String, String[]> secureParameterMap = this.secureServletRequest.getParameterMap();
    String[] secureParameter = secureParameterMap.get("q");
    assertEquals(EXCEPTED_VALUE, secureParameter[0]);
    assertEquals("you", secureParameter[1]);
    verify(this.request);
  }

  @Test
  public void testGetParameterMapNull() {
    Map<String, String[]> parameterMap = new HashMap<>();
    parameterMap.put("q", null);
    expect(this.request.getParameterMap()).andReturn(parameterMap);
    replay(this.request);
    Map<String, String[]> secureParameterMap = this.secureServletRequest.getParameterMap();
    String[] secureParameter = secureParameterMap.get("q");
    assertSame(null, secureParameter);
    verify(this.request);
  }

}
