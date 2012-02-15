package edu.stanford.irt.laneweb.bookmarks;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.irt.laneweb.model.Model;

@Controller
@RequestMapping(value = "/bookmarklet", method = RequestMethod.GET, consumes = "application/x-www-form-urlencoded")
public class BookmarkletController extends BookmarkController {
    
    @ResponseStatus(value = HttpStatus.OK)
    public void addBookmark(
            @ModelAttribute(Model.BOOKMARKS) final List<Bookmark> bookmarks,
            @ModelAttribute(Model.SUNETID) final String sunetid,
            @RequestBody final Bookmark bookmark) {
        bookmarks.add(0, bookmark);
        saveLinks(sunetid, bookmarks);
    }
}
