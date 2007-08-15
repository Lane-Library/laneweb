/**
 * 
 */
package edu.stanford.irt.laneweb;

import java.io.StringReader;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.TransformerException;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamSource;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.configuration.ConfigurationException;
import org.apache.cocoon.serialization.XMLSerializer;
import org.xml.sax.SAXException;

/**
 * @author ceyates
 *
 */
public class XHTMLSerializer extends XMLSerializer {

    public void configure(Configuration conf)
    throws ConfigurationException {
        super.configure( conf );
        this.format.put(OutputKeys.METHOD,"xhtml");
    }

    protected TransformerHandler getTransformerHandler() throws TransformerException {
        return this.getTransformerFactory().newTransformerHandler(new StreamSource(new StringReader(foo)));
    }
    
    //TODO add <![CDATA[]]>
    private static final String foo = "<stylesheet version='2.0' "+
    "xmlns='http://www.w3.org/1999/XSL/Transform' xmlns:h='http://www.w3.org/1999/xhtml'>"+
    "<template match='*'><copy><apply-templates select='@*|*|text()'/></copy></template>"+
    "<template match='@*'><copy-of select='.'/></template>"+
    "<template match='h:script'><copy>"+
    "<apply-templates select='@*'/><value-of select='.' disable-output-escaping='yes'/>"+
    "</copy></template>"+
    "</stylesheet>";

}
