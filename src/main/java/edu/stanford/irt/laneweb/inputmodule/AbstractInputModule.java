package edu.stanford.irt.laneweb.inputmodule;

import java.util.Iterator;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.configuration.Configuration;
import org.apache.cocoon.components.modules.input.InputModule;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.log4j.Logger;

import edu.stanford.irt.laneweb.user.User;
import edu.stanford.irt.laneweb.user.UserDao;

public abstract class AbstractInputModule implements InputModule {
    
    private Logger logger = Logger.getLogger(AbstractInputModule.class);

    private UserDao userDao;

    @SuppressWarnings("unchecked")
    public Object getAttribute(final String key, final Configuration modeConf, final Map objectModel) {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        User user = this.userDao.createOrUpdateUser(request);
        if (this.logger.isDebugEnabled()) {
            Object result = doGetAttribute(key, user, request);
            this.logger.debug(key + " = " + result);
            return result;
        } else {
            return doGetAttribute(key, user, request);
        }
    }

    @SuppressWarnings("unchecked")
    public Iterator<String> getAttributeNames(final Configuration key, final Map config) {
        throw new UnsupportedOperationException();
    }

    @SuppressWarnings("unchecked")
    public Object[] getAttributeValues(final String name, final Configuration modeConf, final Map objectModel) {
        throw new UnsupportedOperationException();
    }

    public void setUserDao(final UserDao userDao) {
        if (null == userDao) {
            throw new IllegalArgumentException("null userDao");
        }
        this.userDao = userDao;
    }

    protected abstract Object doGetAttribute(String key, User user, HttpServletRequest request);
}
