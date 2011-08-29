package edu.stanford.irt.laneweb.servlet;

import static org.easymock.EasyMock.createMock;
import static org.easymock.EasyMock.expect;
import static org.easymock.EasyMock.replay;
import static org.easymock.EasyMock.verify;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.junit.Before;
import org.junit.Test;

public class ShowParameterRedirectFilterTest {

    private FilterChain chain;

    private ShowParameterRedirectFilter filter;

    private HttpServletRequest request;

    private HttpServletResponse response;

    @Before
    public void setUp() throws Exception {
        this.filter = new ShowParameterRedirectFilter();
        this.request = createMock(HttpServletRequest.class);
        this.response = createMock(HttpServletResponse.class);
        this.chain = createMock(FilterChain.class);
    }

    @Test
    public void testDoFilterNoQueryString() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn(null);
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.chain);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request, this.chain);
    }

    @Test
    public void testDoFilterQueryStringNoShow() throws IOException, ServletException {
        expect(this.request.getQueryString()).andReturn("foo=bar&bar=foo");
        this.chain.doFilter(this.request, this.response);
        replay(this.request, this.chain);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request, this.chain);
    }

    @Test
    public void testDoFilterQueryStringShow() throws IOException, ServletException {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("foo", new String[] { "bar" });
        parameterMap.put("show", new String[] { "800" });
        parameterMap.put("bar", new String[] { "foo" });
        expect(this.request.getQueryString()).andReturn("foo=bar&show=800&bar=foo");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer());
        expect(this.request.getParameterMap()).andReturn(parameterMap);
        this.response.setHeader("Location", "?foo=bar&bar=foo");
        this.response.setStatus(301);
        replay(this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request, this.response);
    }

    @Test
    public void testDoFilterQueryStringShowMultipleProxyLinks() throws IOException, ServletException {
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("foo", new String[] { "bar" });
        parameterMap.put("proxy-links", new String[] { "true", "false", "false" });
        parameterMap.put("show", new String[] { "800" });
        parameterMap.put("bar", new String[] { "foo" });
        expect(this.request.getQueryString()).andReturn(
                "foo=bar&proxy-links=true&show=800&bar=foo&proxy-links=false&proxy-links=false");
        expect(this.request.getRequestURL()).andReturn(new StringBuffer());
        expect(this.request.getParameterMap()).andReturn(parameterMap);
        this.response.setHeader("Location", "?foo=bar&bar=foo&proxy-links=true");
        this.response.setStatus(301);
        replay(this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request, this.response);
    }

    @Test
    public void testDoFilterShowWithAmps() throws ServletException, IOException {
        String queryString = "amp;amp;amp;show=140&a=n&amp;amp;proxy-links=false&amp;amp;show=200&proxy-links=false&amp;page=2";
        Map<String, String[]> parameterMap = new HashMap<String, String[]>();
        parameterMap.put("amp;amp;amp;show", new String[] { "140" });
        parameterMap.put("a", new String[] { "n" });
        parameterMap.put("amp;amp;proxy-links", new String[] { "false" });
        parameterMap.put("amp;amp;show", new String[] { "200" });
        parameterMap.put("proxy-links", new String[] { "false" });
        parameterMap.put("amp;page", new String[] { "2" });
        expect(this.request.getQueryString()).andReturn(queryString);
        expect(this.request.getRequestURL()).andReturn(new StringBuffer("/biomed-resources/ej.html"));
        expect(this.request.getParameterMap()).andReturn(parameterMap);
        this.response.setHeader("Location", "/biomed-resources/ej.html?a=n&proxy-links=false");
        this.response.setStatus(301);
        replay(this.request, this.response);
        this.filter.doFilter(this.request, this.response, this.chain);
        verify(this.request, this.response);
    }
}
