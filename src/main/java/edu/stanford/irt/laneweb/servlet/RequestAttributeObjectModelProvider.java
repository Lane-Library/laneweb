package edu.stanford.irt.laneweb.servlet;

import org.apache.cocoon.el.objectmodel.ObjectModelProvider;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import edu.stanford.irt.laneweb.model.Model;

/**
 * @author ceyates $Id$
 */
public class RequestAttributeObjectModelProvider implements ObjectModelProvider {

    public Object getObject() {
        return RequestContextHolder.getRequestAttributes().getAttribute(Model.MODEL, RequestAttributes.SCOPE_REQUEST);
        // TODO: introducing Spring dependency here . . .
    }
}
