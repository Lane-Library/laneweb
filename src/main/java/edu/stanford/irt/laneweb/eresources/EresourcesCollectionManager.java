package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Calendar;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.eresources.Eresource;
import edu.stanford.irt.eresources.impl.EresourceImpl;
import edu.stanford.irt.eresources.impl.LinkImpl;
import edu.stanford.irt.eresources.impl.VersionImpl;

public class EresourcesCollectionManager extends AbstractCollectionManager {

    protected static final int THIS_YEAR = Calendar.getInstance().get(Calendar.YEAR);

    public EresourcesCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
        super(dataSource, sqlStatements);
    }

    @Override
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        EresourceImpl eresource = null;
        VersionImpl version = null;
        LinkImpl link;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        String currentTitle = null;
        String trimmedQuery = (query != null) ? query.trim() : null;
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
                if (trimmedQuery != null) {
                    if (trimmedQuery.equalsIgnoreCase(currentTitle)) {
                        eresource.setScore(Integer.MAX_VALUE);
                    } else {
                        // core material weighted * 3
                        int coreFactor = "Y".equals(rs.getString("CORE")) ? 3 : 1;
                        // weighted oracle text scores for title and text
                        // averaged
                        int scoreFactor = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
                        int year = rs.getInt("YEAR");
                        // subtract number of years difference from current year
                        // yearFactor can change score from -10 to 10 points
                        int yearFactor = year == 0 ? 0 : Math.max(-10, 10 - (THIS_YEAR - year));
                        eresource.setScore(scoreFactor + yearFactor);
                    }
                }
                eresource.setDescription(rs.getString("E_DESCRIPTION"));
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
                currentVersionId = -1;
                currentLinkId = -1;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
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
                link = new LinkImpl();
                version.addLink(link);
                link.setUrl(rs.getString("URL"));
                link.setLabel(rs.getString("LABEL"));
                link.setInstruction(rs.getString("INSTRUCTION"));
                currentLinkId = rowLinkId;
            }
        }
        return eresources;
    }
}
