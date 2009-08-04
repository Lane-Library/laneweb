package edu.stanford.irt.laneweb;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Request;
import org.apache.cocoon.processing.ProcessInfoProvider;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;


public class LanewebObjectModelProvider implements ObjectModelProvider {
    
    private ProcessInfoProvider processInfoProvider;
    private Map<String, Object> jndiData;
    private UserDao userDao;
    
    public LanewebObjectModelProvider(ProcessInfoProvider pip, Map<String, Object> jndiData, UserDao userDao) {
        this.processInfoProvider = pip;
        this.jndiData = jndiData;
        this.userDao = userDao;
    }

    public Object getObject() {
        Map objectModel = processInfoProvider.getObjectModel();
        
        Map cocoonMap = new HashMap();
        
        //cocoon.request
        Request request = ObjectModelHelper.getRequest(objectModel);
        cocoonMap.put("request", request);
        
        //cocoon.session
        HttpSession session = request.getSession(true);
        cocoonMap.put("session", session);
        User user = (User) session.getAttribute(LanewebConstants.USER);
        if (null == user) {
            user = new User();
            session.setAttribute(LanewebConstants.USER, user);
        }
        this.userDao.getUserData(user, request);
        
        // cocoon.context
        org.apache.cocoon.environment.Context context = ObjectModelHelper.getContext(objectModel);
        cocoonMap.put("context", context);
        
        cocoonMap.put("jndi", this.jndiData);
        
        return cocoonMap;
    }
}
