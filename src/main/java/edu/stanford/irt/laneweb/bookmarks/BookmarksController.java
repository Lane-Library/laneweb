package edu.stanford.irt.laneweb.bookmarks;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.SessionAttributes;

import edu.stanford.irt.laneweb.model.Model;

@Controller
@SessionAttributes(Model.BOOKMARKS)
public class BookmarksController {

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void addLink(@RequestParam final String label, @RequestParam final String url,
            @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.add(new Bookmark(label, url));
    }

    @ModelAttribute(Model.BOOKMARKS)
    public Bookmarks getBookmarks() {
        return new Bookmarks();
    }

    @RequestMapping(value = "get")
    @ResponseBody
    public Bookmarks getBookmarks(@ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        return bookmarks;
    }

    @ExceptionHandler(IndexOutOfBoundsException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleIndexOutOfBounds(final IndexOutOfBoundsException ex, final HttpServletRequest request) {
        return ex.toString();
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    @ResponseBody
    public String handleMissingParameter(final MissingServletRequestParameterException ex, final HttpServletRequest request) {
        return ex.toString();
    }

    @RequestMapping(value = "/remove", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void removeBookmark(@RequestParam final int position, @ModelAttribute(Model.BOOKMARKS) final Bookmarks bookmarks) {
        bookmarks.remove(position);
    }
}
