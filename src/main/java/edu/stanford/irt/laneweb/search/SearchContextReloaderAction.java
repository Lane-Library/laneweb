package edu.stanford.irt.laneweb.search;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.acting.Action;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.Redirector;
import org.apache.cocoon.environment.SourceResolver;

public class SearchContextReloaderAction implements Action {

    private MetaSearchManagerSource msms;

    private String svnUrlPath;

    private String svnUrlProject;

    @SuppressWarnings("unchecked")
    public Map act(final Redirector redirector, final SourceResolver sourceResolver, final Map objectModel, final String string, final Parameters param)
            throws Exception {
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        String release = request.getParameter("release");
        String userName = request.getParameter("username");
        String password = request.getParameter("password");
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
