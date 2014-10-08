package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.user.UserAttribute;


public interface UserAttributesRequestParser {

    Map<UserAttribute, String> parse(HttpServletRequest request);
}
