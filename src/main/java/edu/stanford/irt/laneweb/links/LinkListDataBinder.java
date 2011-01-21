package edu.stanford.irt.laneweb.links;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.binding.DataBinder;

public class LinkListDataBinder implements DataBinder {

    private LinkListDAO linkListDAO;

    public void bind(final Map<String, Object> model, final HttpServletRequest request) {
        String sunetid = (String) model.get(Model.SUNETID);
        if (sunetid != null) {
            LinkList linkList = null;
            HttpSession session = request.getSession();
            LinkList sessionLinkList = (LinkList) session.getAttribute("link-list");
            if (sessionLinkList == null) {
                linkList = this.linkListDAO.getLinks(sunetid);
            } else {
                linkList = sessionLinkList;
            }
            if (linkList != null) {
                if (sessionLinkList == null) {
                    session.setAttribute("link-list", linkList);
                }
                model.put("link-list", linkList);
            }
        }
    }

    public void setLinkListDAO(final LinkListDAO linkListDAO) {
        this.linkListDAO = linkListDAO;
    }
}
