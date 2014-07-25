package edu.stanford.irt.laneweb.servlet.mvc;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;

/**
 * @author alainb
 */
@Controller
@RequestMapping(value = "/secure/reloadresources")
public class SearchContextReloaderController {

    private static final String SVN_PATH = "/src/main/resources/search-lane.xml";

    private static final String SVN_PROJECT_URL = "https://irt-svn.stanford.edu/repos/irt/lane/search/tags/search-lane-";

    private MetaSearchManagerSource msms;

    @Autowired
    public SearchContextReloaderController(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    @RequestMapping(method = RequestMethod.POST)
    public String reloadContext(@RequestParam final String release, @RequestParam final String sunetid,
            @RequestParam final String password) throws IOException {
        this.msms.reload(new StringBuilder(SVN_PROJECT_URL).append(release).append(SVN_PATH).toString(), sunetid,
                password);
        return "redirect:/reloadresources.html";
    }
}
