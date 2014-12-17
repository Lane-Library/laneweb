package edu.stanford.irt.laneweb.xsl;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;


public abstract class AbstractXSLTest {

    protected String getExpectedResult(final String fileName) throws IOException {
        StringWriter sw = new StringWriter();
        InputStreamReader br = new InputStreamReader(getClass().getResourceAsStream(fileName), StandardCharsets.UTF_8);
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
