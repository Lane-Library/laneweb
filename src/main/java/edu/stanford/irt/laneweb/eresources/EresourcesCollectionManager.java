package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import edu.stanford.irt.laneweb.eresources.Eresource.EresourceBuilder;

public class EresourcesCollectionManager extends AbstractCollectionManager {

    private static class ResultSetHandler {

        boolean createGetPassword = false;

        private int currentEresourceId = -1;

        private int currentLinkId = -1;

        private String currentTitle = null;

        private int currentVersionId = -1;

        private List<Eresource> eresources = new LinkedList<Eresource>();

        private boolean isFirstLink = true;

        private String query;

        private ResultSet rs;

        private ScoreStrategy scoreStrategy;
        
        private EresourceBuilder builder;

        public ResultSetHandler(final ResultSet resultSet, final String query, final ScoreStrategy scoreStrategy) {
            this.rs = resultSet;
            this.query = query;
            this.scoreStrategy = scoreStrategy;
        }

        public List<Eresource> parseResultSet() throws SQLException {
            while (this.rs.next()) {
                int rowEresourceId = this.rs.getInt("ERESOURCE_ID");
                int recordId = this.rs.getInt("RECORD_ID");
                String recordType = this.rs.getString("RECORD_TYPE");
                String rowTitle = this.rs.getString("TITLE");
                rowTitle = rowTitle == null ? "MISSING TITLE" : rowTitle;
                int rowVersionId = this.rs.getInt("VERSION_ID");
                int rowLinkId = this.rs.getInt("LINK_ID");
                if ((rowEresourceId != this.currentEresourceId) || !rowTitle.equals(this.currentTitle)) {
                    this.builder = newEresourceBuilder(rowTitle, rowEresourceId, recordId, recordType);
                }
                if (rowVersionId != this.currentVersionId) {
                    this.createGetPassword = "T".equals(this.rs.getString("GETPASSWORD"));
                    this.currentVersionId = rowVersionId;
                    this.currentLinkId = -1;
                }
                if (rowLinkId != this.currentLinkId) {
                    addLink(rowTitle, rowLinkId);
                }
            }
            if (this.builder != null) {
                this.eresources.add(this.builder.build());
            }
            return this.eresources;
        }

        private EresourceBuilder newEresourceBuilder(final String title, final int resourceId, final int recordId, final String recordType)
                throws SQLException {
            if (this.builder != null) {
                this.eresources.add(this.builder.build());
            }
            this.currentTitle = title;
            int score = this.query == null ? 0 : this.scoreStrategy
                    .computeScore(this.query, this.currentTitle, this.rs);
            this.builder = Eresource.builder();
            this.currentEresourceId = resourceId;
            this.currentVersionId = -1;
            this.currentLinkId = -1;
            this.isFirstLink = true;
            return Eresource.builder().description(this.rs.getString("DESCRIPTION")).id(Integer.toString(resourceId))
                    .recordId(recordId).recordType(recordType).score(score).title(this.currentTitle)
                    .primaryType(this.rs.getString("PRIMARY_TYPE")).total(this.rs.getInt("TOTAL"))
                    .available(this.rs.getInt("AVAILABLE"));
        }

        private void addLink(final String title, final int linkId) throws SQLException {
            // determine the link type from the label
            String label = this.rs.getString("LABEL");
            LinkType type = null;
            if (this.createGetPassword) {
                type = LinkType.GETPASSWORD;
                this.createGetPassword = false;
            } else if (label != null && "impact factor".equalsIgnoreCase(label)) {
                type = LinkType.IMPACTFACTOR;
            } else {
                type = LinkType.NORMAL;
            }
            String linkText = this.isFirstLink ? title : this.rs.getString("LINK_TEXT");
            String additionalText = this.isFirstLink ? this.rs.getString("V_ADDITIONAL_TEXT") : this.rs
                    .getString("L_ADDITIONAL_TEXT");
            this.builder.addLink(new Link(label, type, this.rs.getString("URL"), linkText, additionalText, this.rs
                    .getString("PUBLISHER"), this.rs.getString("HOLDINGS_DATES")));
            this.currentLinkId = linkId;
            this.isFirstLink = false;
        }
    }

    private ScoreStrategy scoreStrategy;

    public EresourcesCollectionManager(final DataSource dataSource, final Properties sqlStatements,
            final ScoreStrategy strategy) {
        super(dataSource, sqlStatements);
        this.scoreStrategy = strategy;
    }

    @Override
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        return new ResultSetHandler(rs, query, this.scoreStrategy).parseResultSet();
    }
}
