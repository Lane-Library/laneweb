package edu.stanford.irt.laneweb.links;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

public class LinkListHandlerInterceptor extends HandlerInterceptorAdapter {

    private LinkListDAO linkListDAO;

    private SunetIdSource sunetIdSource = new SunetIdSource();

    @Override
    public boolean preHandle(final HttpServletRequest request, final HttpServletResponse response, final Object handler)
            throws Exception {
        String sunetid = this.sunetIdSource.getSunetid(request);
        if (sunetid != null) {
            HttpSession session = request.getSession();
            LinkList links = (LinkList) session.getAttribute("link-list");
            if (links != null) {
                String url = request.getParameter("url");
                String label = request.getParameter("label");
                if (url != null && label != null && url.length() > 0 && label.length() > 0) {
                    Link link = new LinkImpl();
                    link.setLabel(label);
                    link.setUrl(url);
                    links.add(link);
                    this.linkListDAO.saveLinks(sunetid, links);
                }
                String delete = request.getParameter("delete");
                if (delete != null && delete.length() > 0) {
                    links.remove(Integer.parseInt(delete));
                    this.linkListDAO.saveLinks(sunetid, links);
                }
            } else {
                String create = request.getParameter("create");
                if ("true".equals(create)) {
                    this.linkListDAO.saveLinks(sunetid, new LinkList());
                }
            }
        }
        return true;
    }

    public void setLinkListDAO(final LinkListDAO linkListDAO) {
        this.linkListDAO = linkListDAO;
    }
}
