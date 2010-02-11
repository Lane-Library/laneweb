package edu.stanford.irt.laneweb.search;

import java.util.Map;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

/**
 * 
 * @author alainb
 * 
 * $Id$
 *
 */
public class SearchContextReloaderAction implements Action {

    private MetaSearchManagerSource msms;

    private String svnUrlPath;

    private String svnUrlProject;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel,
            final String string, final Parameters param) {
        String release = param.getParameter("release", null);
        String userName = param.getParameter("username", null);
        String password = param.getParameter("password", null);
        if (release != null && userName != null && password != null) {
            this.msms.reload(this.svnUrlProject.concat(release).concat(this.svnUrlPath), userName, password);
        }
        return null;
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
