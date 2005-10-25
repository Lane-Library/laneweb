package edu.stanford.laneweb;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;
import java.util.Map;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.avalon.framework.thread.ThreadSafe;
import org.apache.cocoon.components.modules.input.AbstractInputModule;
import org.apache.cocoon.environment.ObjectModelHelper;

public class URIEncodedParameterModule extends AbstractInputModule implements ThreadSafe {

    public Object getAttribute( String name, Configuration modeConf, Map objectModel ) {

        String pname = (String) this.settings.get("parameter",name);
        Object obj = ObjectModelHelper.getRequest(objectModel).get( pname );
        
        if (obj == null) {
            return null;
        }
        try {
            return URLEncoder.encode((String)obj, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return null;
        }
    }


    public Iterator getAttributeNames( Configuration modeConf, Map objectModel ) {

        throw new UnsupportedOperationException("cant do this");
    }


    public Object[] getAttributeValues( String name, Configuration modeConf, Map objectModel ) {
        throw new UnsupportedOperationException("cant do this either");
    }

}
