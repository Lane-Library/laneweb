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

public class ResourceList2HtmlTest extends AbstractXSLTest {

    private Source source;

    private Transformer transformer;

    @BeforeEach
    public void setUp() throws Exception {
        this.transformer = TransformerFactory.newInstance()
                .newTransformer(new StreamSource("src/main/xsl/resourceList2html.xsl"));
        this.source = new StreamSource(getClass().getResourceAsStream("ResourceList2HtmlTest.xml"));
        this.transformer.setOutputProperty("indent", "yes");
        this.transformer.setOutputProperty("method", "xml");
    }

    @Test
    public void testTransform() throws TransformerException, IOException {
        StringWriter sw = new StringWriter();
        Result result = new StreamResult(sw);
        this.transformer.setParameter("url-encoded-query", "music");
        this.transformer.transform(this.source, result);
        assertEquals(getExpectedResult("resources.xml"), sw.toString());
    }
}