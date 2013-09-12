package edu.stanford.irt.laneweb.xsl;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;

import javax.xml.transform.Result;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.junit.Before;
import org.junit.Test;

import edu.stanford.irt.laneweb.model.Model;


public class Test_laneweb_login {
    
    private Transformer transformer;
    
    private Source source;

    @Before
    public void setUp() throws Exception {
        this.transformer = TransformerFactory.newInstance().newTransformer(new StreamSource("src/main/webapp/resources/xsl/laneweb.xsl"));
        this.source = new StreamSource(getClass().getResourceAsStream("login.xml"));
        this.transformer.setOutputProperty("indent", "yes");
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
    public void testTrueSunetidSOM() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.SUNETID, "sunetid");
        this.transformer.setParameter(Model.IPGROUP, "SOM");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueSunetidSOM.xml"), sw.toString());
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
    public void testQueryProxyLinksTrue() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=true");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("TrueZeroOther.xml"), sw.toString());
    }

    @Test
    public void testQuerySomethingElseProxyLinksTrue() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.PROXY_LINKS, "true");
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "something=else&proxy-links=true");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("QuerySomethingElseProxyLinksTrue.xml"), sw.toString());
    }

//    @Test
    public void testProxyLinksAndPageParameters() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter(Model.IPGROUP, "OTHER");
        this.transformer.setParameter(Model.QUERY_STRING, "proxy-links=false&page=1");
        this.transformer.setParameter(Model.PROXY_LINKS, "false");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("SOMProxyLinksTrue.xml"), sw.toString());
    }

    public String getExpectedResult(final String fileName) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader br = new InputStreamReader(getClass().getResourceAsStream(fileName), "UTF-8");
        char[] cbuf = new char[1024];
        while (true) {
            int i = br.read(cbuf);
            if (i == -1) {
                break;
            }
            sw.write(cbuf, 0, i);
        }
        return sw.toString();
    }
}
