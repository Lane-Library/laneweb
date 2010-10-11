package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

public class MobileRemoteAddressDataBinder extends  RemoteAddressDataBinder {

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        model.put(Model.REMOTE_ADDR, getRemoteAddress(request));
    }
}
