package edu.stanford.irt.laneweb.bookmarks;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.SunetIdSource;

@Controller
@RequestMapping(value = "/bookmarks")
public class BookmarkController {
    
    private SunetIdSource sunetidSource = new SunetIdSource();;

    @Autowired
    private BookmarkDAO<Bookmark> bookmarkDAO;

    @RequestMapping(value = "/add")
    public void addBookmark(final HttpServletRequest request, final HttpServletResponse response,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks, @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestBody final Map<String, String> json) {
        bookmarks.add(0, new Bookmark(json.get("title"), json.get("url")));
        this.bookmarkDAO.saveLinks(sunetid, bookmarks);
    }

    @SuppressWarnings("unchecked")
    @ModelAttribute(Model.BOOKMARKS)
    public List<Bookmark> getBookmarks(final HttpSession session, @ModelAttribute(Model.SUNETID) final String sunetid) {
        List<Bookmark> bookmarks = (List<Bookmark>) session.getAttribute(Model.BOOKMARKS);
        if (bookmarks == null) {
            bookmarks = this.bookmarkDAO.getLinks(sunetid);
            if (bookmarks == null) {
                bookmarks = new ArrayList<Bookmark>();
            }
            session.setAttribute(Model.BOOKMARKS, bookmarks);
        }
        return bookmarks;
    }
    
    @ModelAttribute(Model.SUNETID)
    public String getSunetid(HttpServletRequest request) {
        return this.sunetidSource.getSunetid(request);
    }

    @RequestMapping(params = "action=add")
    public void handleAddBookmark(final HttpServletRequest request, final HttpServletResponse response,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks, @ModelAttribute(Model.SUNETID) final String sunetid)
            throws ServletException, IOException {
        bookmarks.add(0, new Bookmark("", ""));
        this.bookmarkDAO.saveLinks(sunetid, bookmarks);
        request.getRequestDispatcher("/samples/favorites.html?action=edit&i=0").forward(request, response);
    }

    @RequestMapping(params = "action=delete")
    public void handleDeleteBookmarks(final HttpServletRequest request, final HttpServletResponse response,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks, @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final int[] i) throws ServletException, IOException {
        Arrays.sort(i);
        for (int j = i.length - 1; j >= 0; j--) {
            bookmarks.remove(i[j]);
        }
        this.bookmarkDAO.saveLinks(sunetid, bookmarks);
        request.getRequestDispatcher("/samples/favorites.html").forward(request, response);
    }

    @RequestMapping(params = "action=edit")
    public void handleEditBookmark(final HttpServletRequest request, final HttpServletResponse response) throws ServletException,
            IOException {
        request.getRequestDispatcher("/samples/favorites.html").forward(request, response);
    }

    @RequestMapping(params = "action=save")
    public void handleSaveBookmark(final HttpServletRequest request, final HttpServletResponse response,
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks, @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestParam final int i, @RequestParam final String label, @RequestParam final String url) throws ServletException,
            IOException {
        bookmarks.set(i, new Bookmark(label, url));
        this.bookmarkDAO.saveLinks(sunetid, bookmarks);
        request.getRequestDispatcher("/samples/favorites.html").forward(request, response);
    }

    public void setBookmarkDAO(final BookmarkDAO<Bookmark> bookmarkDAO) {
        this.bookmarkDAO = bookmarkDAO;
    }
}
