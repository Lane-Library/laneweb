package edu.stanford.irt.laneweb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.processing.ProcessInfoProvider;


public class LanewebObjectModelProvider implements ObjectModelProvider {
    
    private ProcessInfoProvider processInfoProvider;
    private Map<String, Object> jndiData;
    
    public LanewebObjectModelProvider(ProcessInfoProvider pip, Map<String, Object> jndiData) {
        this.processInfoProvider = pip;
        this.jndiData = jndiData;
    }

    public Object getObject() {
        Map objectModel = processInfoProvider.getObjectModel();
        
        Map cocoonMap = new HashMap();
        
        //cocoon.request
        Request request = ObjectModelHelper.getRequest(objectModel);
        cocoonMap.put("request", request);
        
        //cocoon.session
        HttpSession session = request.getSession(false);
        if (session != null) {
            cocoonMap.put("session", session);
        }
        
        // cocoon.context
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        cocoonMap.put("context", context);
        
        cocoonMap.put("jndi", this.jndiData);
        
        return cocoonMap;
    }
}
