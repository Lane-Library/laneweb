package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.IPGroup;
import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.ProxyLinks;


public class ProxyLinksDataBinder extends BooleanSessionParameterDataBinder {
    
    private ProxyLinks proxyLinks;

    @Override
    public void bind(Map<String, Object> model, HttpServletRequest request) {
        super.bind(model, request);
        if (!model.containsKey(Model.PROXY_LINKS)) {
            HttpSession session = request.getSession();
            IPGroup ipGroup = (IPGroup) model.get(Model.IPGROUP);
            String remoteAddress = (String) model.get(Model.REMOTE_ADDR);
            Boolean proxyLinks = this.proxyLinks.getProxyLinks(request, session, ipGroup, remoteAddress);
            session.setAttribute(Model.PROXY_LINKS, proxyLinks);
            model.put(Model.PROXY_LINKS, proxyLinks);
        }
    }
    
    public void setProxyLinks(ProxyLinks proxyLinks) {
        this.proxyLinks = proxyLinks;
    }
}
