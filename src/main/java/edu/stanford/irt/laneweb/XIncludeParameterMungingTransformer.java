package edu.stanford.irt.laneweb;

import java.io.Serializable;
import java.util.Map;
import java.util.StringTokenizer;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.caching.CacheableProcessingComponent;
import org.apache.cocoon.environment.SourceResolver;
import org.apache.cocoon.transformation.AbstractTransformer;
import org.apache.cocoon.transformation.XIncludeTransformer;
import org.apache.excalibur.source.SourceValidity;
import org.apache.excalibur.source.impl.validity.NOPValidity;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

//FIXME: this is a temporary bridge to remove cocoon: protocol requests with parameters.
public class XIncludeParameterMungingTransformer extends AbstractTransformer implements CacheableProcessingComponent {

    @Override
    public void startElement(String uri, String loc, String raw, Attributes a) throws SAXException {
        if (XIncludeTransformer.XINCLUDE_NAMESPACE_URI.equals(uri) && "include".equals(loc)) {
            String href = a.getValue("href");
            if (null != href && href.indexOf("cocoon:") == 0 && href.indexOf('?') > 0) {
                AttributesImpl newAttributes = new AttributesImpl(a);
                StringTokenizer st = new StringTokenizer(href, "?=");
                StringBuilder builder = new StringBuilder(st.nextToken());
                while (st.hasMoreTokens()) {
                    String token = st.nextToken();
                    if ("t".equals(token)) {
                        token = "type";
                    } else if ("s".equals(token)) {
                        token = "subset";
                    }
                    if (!"category".equals(token) && !"source".equals(token)) {
                        builder.append('/').append(token);
                    }
                }
                newAttributes.setValue(newAttributes.getIndex("href"), builder.toString());
                this.xmlConsumer.startElement(uri, loc, raw, newAttributes);
                return;
            }
        }
        this.xmlConsumer.startElement(uri, loc, raw, a);
    }

    public void setup(SourceResolver resolver, Map objectModel, String src, Parameters par) {
    }

    public Serializable getKey() {
        return "xim";
    }

    public SourceValidity getValidity() {
        return NOPValidity.SHARED_INSTANCE;
    }
}
