package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.Version;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.eresources.impl.VersionImpl;

public class EresourcesCollectionManager extends AbstractCollectionManager {

    private ScoreStrategy scoreStrategy;

    public EresourcesCollectionManager(final DataSource dataSource, final Properties sqlStatements, final ScoreStrategy strategy) {
        super(dataSource, sqlStatements);
        this.scoreStrategy = strategy;
    }

    @Override
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        Eresource eresource = null;
        Version version = null;
        TypedLink link;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        String currentTitle = null;
        boolean createGetPassword = false;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            int recordId = rs.getInt("RECORD_ID");
            String recordType = rs.getString("RECORD_TYPE");
            String rowTitle = rs.getString("TITLE");
            if ((rowEresourceId != currentEresourceId) || !rowTitle.equals(currentTitle)) {
                currentTitle = rowTitle;
                eresource = new EresourceImpl();
                eresource.setId(rowEresourceId);
                eresource.setRecordId(recordId);
                eresource.setRecordType(recordType);
                eresource.setTitle(currentTitle);
                if (query != null) {
                    eresource.setScore(this.scoreStrategy.computeScore(query, currentTitle, rs));
                }
                eresource.setDescription(rs.getString("E_DESCRIPTION"));
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
                currentVersionId = -1;
                currentLinkId = -1;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
                createGetPassword = "T".equals(rs.getString("GETPASSWORD"));
                version = new VersionImpl();
                eresource.addVersion(version);
                version.setPublisher(rs.getString("PUBLISHER"));
                version.setSummaryHoldings(rs.getString("HOLDINGS"));
                version.setDates(rs.getString("DATES"));
                version.setDescription(rs.getString("V_DESCRIPTION"));
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
                link = new TypedLink();
                link.setType(type);
                link.setUrl(rs.getString("URL"));
                link.setLabel(label);
                link.setInstruction(rs.getString("INSTRUCTION"));
                version.addLink(link);
                currentLinkId = rowLinkId;
            }
        }
        return eresources;
    }
}
