package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.Model;

/**
 * @author alainb $Id$
 */
public class SearchContextReloaderAction extends AbstractAction {

    private MetaSearchManagerSource msms;

    private String svnUrlPath;

    private String svnUrlProject;

    @Override
    public Map<String, String> doAct() {
        String release = this.model.getString(Model.RELEASE);
        String sunetid = this.model.getString(Model.SUNETID);
        String password = this.model.getString(Model.PASSWORD);
        if (release != null && sunetid != null && password != null) {
            this.msms.reload(this.svnUrlProject.concat(release).concat(this.svnUrlPath), sunetid, password);
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
