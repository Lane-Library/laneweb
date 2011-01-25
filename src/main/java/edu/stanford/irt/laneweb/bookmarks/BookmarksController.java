package edu.stanford.irt.laneweb.bookmarks;

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
@SessionAttributes({ Model.BOOKMARKS, Model.SUNETID })
@RequestMapping( value = "/bookmarks" )
public class BookmarksController {

    @Autowired
    private BookmarksDAO bookmarksDAO;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void addLink(@RequestParam final String label, @RequestParam final String url,
            @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.add(new Bookmark(label, url));
    }

    @RequestMapping(value = "get")
    @ResponseBody
    public Bookmarks getBookmarks(@ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        return bookmarks;
    }

    @ModelAttribute(Model.BOOKMARKS)
    public Bookmarks getBookmarks(@ModelAttribute(Model.SUNETID) final String sunetid) {
        return this.bookmarksDAO.getBookmarks(sunetid);
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeBookmark(@RequestParam final int position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.remove(position);
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
