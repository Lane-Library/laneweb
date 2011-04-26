package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import edu.stanford.irt.laneweb.search.MetaSearchManagerSource;

/**
 * @author alainb
 */
@Controller
@RequestMapping(value = "/secure/reloadresources")
public class SearchContextReloaderController {

    @Autowired
    private MetaSearchManagerSource msms;

    private String svnUrlPath = "/src/main/resources/search-lane.xml";

    private String svnUrlProject = "https://irt-svn.stanford.edu/repos/irt/lane/search/tags/search-lane-";

    
    @RequestMapping(method=RequestMethod.POST)
    public ModelAndView reloadContext(@RequestParam final String release, @RequestParam final String sunetid, @RequestParam final String password) {
      if(!"".equals(release) && !"".equals(sunetid) && !"".equals(password))
          this.msms.reload(this.svnUrlProject.concat(release).concat(this.svnUrlPath), sunetid, password);
        return new ModelAndView("/reloadresources.html");
    }
    
    @RequestMapping(method=RequestMethod.GET)
    public ModelAndView view() {
        return new ModelAndView("/reloadresources.html");
    }
    
    
    public void setMetaSearchManagerSource(final MetaSearchManagerSource msms) {
        this.msms = msms;
    }

    public void setSvnUrlPath(final String svnUrlPath) {
        this.svnUrlPath = svnUrlPath;
    }

    public void setSvnUrlProject(final String svnUrlProject) {
        this.svnUrlProject = svnUrlProject;
    }
}
