package edu.stanford.irt.laneweb.xsl;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.stanford.irt.laneweb.model.Model;

public class LanewebLoginTest extends AbstractXSLTest {

    private Source source;

    private Transformer transformer;

    @BeforeEach
    public void setUp() throws Exception {
        this.transformer = TransformerFactory.newInstance()
                .newTransformer(new StreamSource("src/main/xsl/laneweb.xsl"));
        this.source = new StreamSource(getClass().getResourceAsStream("login.xml"));
        this.transformer.setOutputProperty("indent", "yes");
    }

    @Test
    public void testFalseProxyLinksAndPageParameters() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=false&page=1");
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseProxyLinksAndPageParameters.xml"), sw.toString());
    }

    @Test
    public void testFalseQueryProxyLinks() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=false");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseZeroOtherProxyLink.xml"), sw.toString());
    }

    @Test
    public void testFalseQuerySomethingElse() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "something=else");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseQuerySomethingElse.xml"), sw.toString());
    }

    @Test
    public void testFalseQuerySomethingElseProxyLinks() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "something=else&proxy-links=false&param=onemore");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseQuerySomethingElseProxyLinks.xml"), sw.toString());
    }

    @Test
    public void testFalseUserIdSOM() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.USER_ID, "sunetid@stanford.edu");
        this.transformer.setParameter(Model.IPGROUP, "SOM");
        this.transformer.setParameter(Model.SERVLET_PATH, "/redirectPath");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseUserIdSOM.xml"), sw.toString());
    }

    @Test
    public void testFalseZeroOther() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseZeroOther.xml"), sw.toString());
    }

    @Test
    public void testFalseZeroSOM() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.setParameter(Model.IPGROUP, "SOM");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("FalseZeroSOM.xml"), sw.toString());
    }

    @Test
    public void testTrueProxyLinksAndPageParameters() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=true&page=1");
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueProxyLinksAndPageParameters.xml"), sw.toString());
    }

    @Test
    public void testTrueQueryProxyLinks() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=true");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueZeroOtherWithQueryString.xml"), sw.toString());
    }

    @Test
    public void testTrueQuerySomethingElse() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "something=else");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueQuerySomethingElseWithoutProxyLinks.xml"), sw.toString());
    }

    @Test
    public void testTrueQuerySomethingElseProxyLinks() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "something=else&proxy-links=true");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueQuerySomethingElseProxyLinks.xml"), sw.toString());
    }

    @Test
    public void testTrueZeroOther() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueZeroOther.xml"), sw.toString());
    }

    @Test
    public void testTrueZeroSOM() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "SOM");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueZeroSOM.xml"), sw.toString());
    }

    @Test
    public void testUnauthorized() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.REQUEST_URI, "/base/error_authz.html");
        this.transformer.setParameter(Model.SERVLET_PATH, "/error_authz.html");
        this.transformer.setParameter(Model.BASE_PATH, "/base");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("Unauthorized.xml"), sw.toString());
    }

    @Test
    public void testUrlRedirect() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.QUERY_STRING, "something=else&proxy-links=true&param=onemore");
        this.transformer.setParameter(Model.SERVLET_PATH, "/redirectPath");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("urlRedirect.xml"), sw.toString());
    }
}
