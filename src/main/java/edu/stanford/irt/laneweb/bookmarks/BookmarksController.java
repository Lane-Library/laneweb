package edu.stanford.irt.laneweb.bookmarks;

//import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.model.Model;

@Controller
@SessionAttributes({ Model.BOOKMARKS, Model.EMRID })
@RequestMapping(value = "/bookmarks")
public class BookmarksController {

    @Autowired
    private BookmarksDAO bookmarksDAO;

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void addBookmark(@RequestParam final String label, @RequestParam final String url,
            @RequestParam(required = false) final Integer position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.add(position, new Bookmark(label, url));
        this.bookmarksDAO.saveBookmarks(bookmarks);
    }

    @RequestMapping(value = "get")
    @ResponseBody
    public Bookmarks getBookmarks(@ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        return bookmarks;
    }

    @ModelAttribute(Model.BOOKMARKS)
    public Bookmarks getBookmarks(@ModelAttribute(Model.EMRID) final String emrid) {
        return this.bookmarksDAO.getBookmarks(emrid);
    }

//    @RequestMapping(value = "/moveDown", method = RequestMethod.GET)
//    @ResponseStatus(value = HttpStatus.OK)
//    public void moveDown(@RequestParam final int position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
//        Collections.swap(bookmarks, position, position + 1);
//    }
//
//    @RequestMapping(value = "/moveUp", method = RequestMethod.GET)
//    @ResponseStatus(value = HttpStatus.OK)
//    public void moveUp(@RequestParam final int position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
//        Collections.swap(bookmarks, position, position - 1);
//    }

    @RequestMapping(value = "/delete", method = RequestMethod.POST)
    @ResponseStatus(value = HttpStatus.OK)
    public void deleteBookmark(@RequestParam final int position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.remove(position);
        this.bookmarksDAO.saveBookmarks(bookmarks);
    }

//    @ExceptionHandler(IndexOutOfBoundsException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public String handleIndexOutOfBounds(final IndexOutOfBoundsException ex, final HttpServletRequest request) {
//        return ex.toString();
//    }

//    @ExceptionHandler(MissingServletRequestParameterException.class)
//    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
//    @ResponseBody
//    public String handleMissingParameter(final MissingServletRequestParameterException ex, final HttpServletRequest request) {
//        return ex.toString();
//    }

//    @ExceptionHandler(HttpSessionRequiredException.class)
//    @ResponseStatus(value = HttpStatus.PRECONDITION_FAILED)
//    @ResponseBody
//    public String handleNoSession(final HttpSessionRequiredException ex, final HttpServletRequest request) {
//        return ex.toString();
//    }
}
