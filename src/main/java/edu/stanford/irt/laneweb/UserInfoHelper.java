package edu.stanford.irt.laneweb;

import org.apache.cocoon.environment.Request;

public interface UserInfoHelper {

    public static final String ROLE = UserInfoHelper.class.getName();

    UserInfo getUserInfo(Request request);

}
