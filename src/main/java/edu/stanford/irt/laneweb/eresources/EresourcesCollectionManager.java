package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;


public class EresourcesCollectionManager extends AbstractCollectionManager {

    private ScoreStrategy scoreStrategy;

    public EresourcesCollectionManager(final DataSource dataSource, final Properties sqlStatements, final ScoreStrategy strategy) {
        super(dataSource, sqlStatements);
        this.scoreStrategy = strategy;
    }

    @Override
    // TODO: reduce the complexity of this method
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        List<Eresource> eresources = new LinkedList<Eresource>();
        Eresource eresource = null;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        String currentTitle = null;
        boolean createGetPassword = false;
        boolean isFirstLink = true;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            int recordId = rs.getInt("RECORD_ID");
            String recordType = rs.getString("RECORD_TYPE");
            String rowTitle = rs.getString("TITLE");
            rowTitle = rowTitle == null ? "MISSING TITLE" : rowTitle;
            if ((rowEresourceId != currentEresourceId) || !rowTitle.equals(currentTitle)) {
                currentTitle = rowTitle;
                int score = query == null ? 0 : this.scoreStrategy.computeScore(query, currentTitle, rs);
                eresource = new Eresource(rs.getString("DESCRIPTION"), rowEresourceId, recordId, recordType, score, currentTitle);
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
                currentVersionId = -1;
                currentLinkId = -1;
                isFirstLink = true;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
                createGetPassword = "T".equals(rs.getString("GETPASSWORD"));
                currentVersionId = rowVersionId;
                currentLinkId = -1;
            }
            int rowLinkId = rs.getInt("LINK_ID");
            if (rowLinkId != currentLinkId) {
                // determine the link type from the label
                String label = rs.getString("LABEL");
                LinkType type = null;
                if (createGetPassword) {
                    type = LinkType.GETPASSWORD;
                    createGetPassword = false;
                } else if (label != null && "impact factor".equalsIgnoreCase(label)) {
                    type = LinkType.IMPACTFACTOR;
                } else {
                    type = LinkType.NORMAL;
                }
                String linkText = isFirstLink ? rowTitle : rs.getString("LINK_TEXT");
                String additionalText = isFirstLink ? rs.getString("V_ADDITIONAL_TEXT") : rs.getString("L_ADDITIONAL_TEXT");
                eresource.addLink(new Link(label, type, rs.getString("URL"), linkText, additionalText));
                currentLinkId = rowLinkId;
                isFirstLink = false;
            }
        }
        return eresources;
    }
}
