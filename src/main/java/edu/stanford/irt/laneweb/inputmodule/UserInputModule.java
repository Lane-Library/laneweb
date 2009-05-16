package edu.stanford.irt.laneweb.inputmodule;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.User;

public class UserInputModule extends AbstractInputModule {

    @Override
    protected Object doGetAttribute(final String key, final User user, final HttpServletRequest request) {
        if (User.IPGROUP.equals(key)) {
            return user.getIPGroup();
        }
        if (User.SUNETID.equals(key)) {
            return user.getSunetId();
        }
        if (User.TICKET.equals(key)) {
            return user.getTicket();
        }
        if (User.NAME.equals(key)) {
            return user.getName();
        }
        if (User.EMRID.equals(key)) {
          return user.getEmrId();
        }
        throw new IllegalArgumentException("can't handle key: " + key);
    }
}
