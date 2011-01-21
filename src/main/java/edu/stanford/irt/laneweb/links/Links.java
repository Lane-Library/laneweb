package edu.stanford.irt.laneweb.links;

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

import edu.stanford.irt.eresources.Link;
import edu.stanford.irt.eresources.impl.LinkImpl;

@Controller
@SessionAttributes("links")
public class Links {

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    @ResponseStatus(value = HttpStatus.OK)
    public void addLink(@RequestParam final String label, @RequestParam final String url,
            @ModelAttribute("links") final LinkList links) {
        Link link = new LinkImpl();
        link.setLabel(label);
        link.setUrl(url);
        links.add(link);
    }

    @ModelAttribute("links")
    public LinkList getLinks() {
        return new LinkList();
    }

    @RequestMapping(value = "get")
    @ResponseBody
    public LinkList getLinks(@ModelAttribute("links") final LinkList links) {
        return links;
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
    public void removeLink(@RequestParam final int position, @ModelAttribute("links") final LinkList links) {
        links.remove(position);
    }
}
