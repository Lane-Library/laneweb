package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;

import edu.stanford.irt.laneweb.servlet.binding.SunetIdAndTicketDataBinder;

public abstract class BookmarkController {

    @Autowired
    private BookmarkDAO bookmarkDAO;
    
    @Autowired
    private BookmarkDataBinder bookmarkDataBinder;
    
    @Autowired
    private SunetIdAndTicketDataBinder sunetidTicketDataBinder;

    public void setBookmarkDAO(final BookmarkDAO bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }
    
    @ModelAttribute
    protected void bind(HttpServletRequest request, Model model) {
        this.sunetidTicketDataBinder.bind(model.asMap(), request);
        this.bookmarkDataBinder.bind(model.asMap(), request);
    }

    protected void saveLinks(final String sunetid, final List<Bookmark> links) {
        this.bookmarkDAO.saveLinks(sunetid, links);
    }
}
