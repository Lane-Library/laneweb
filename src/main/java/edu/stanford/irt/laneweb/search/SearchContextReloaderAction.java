package edu.stanford.irt.laneweb.search;

import java.util.Map;

import edu.stanford.irt.laneweb.cocoon.AbstractAction;
import edu.stanford.irt.laneweb.model.LanewebObjectModel;

/**
 * 
 * @author alainb
 * 
 * $Id$
 *
 */
public class SearchContextReloaderAction extends AbstractAction {

    private MetaSearchManagerSource msms;

    private String svnUrlPath;

    private String svnUrlProject;

    public Map doAct() {
        String release = this.model.getString(LanewebObjectModel.RELEASE);
        String sunetid = this.model.getString(LanewebObjectModel.SUNETID);
        String password = this.model.getString(LanewebObjectModel.PASSWORD);
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
