package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;


public class HistoryCollectionManager extends AbstractCollectionManager {

    public HistoryCollectionManager(final DataSource dataSource, final Properties sqlStatements) {
        super(dataSource, sqlStatements);
    }

    @Override
    public List<Eresource> search(final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            params.add(translatedQuery);
        }
        return doGet(SEARCH, params, query);
    }

    @Override
    public List<Eresource> searchType(final String type, final String query) {
        QueryTranslator translator = new QueryTranslator();
        String translatedQuery = translator.translate(query);
        Collection<String> params = new LinkedList<String>();
        for (int i = 0; i < 4; i++) {
            params.add(translatedQuery);
        }
        params.add(type);
        return doGet(SEARCH_TYPE, params, query);
    }

    @Override
    protected List<Eresource> parseResultSet(final ResultSet rs, final String query) throws SQLException {
        LinkedList<Eresource> eresources = new LinkedList<Eresource>();
        Eresource eresource = null;
        Version version = null;
        int currentEresourceId = -1;
        int currentVersionId = -1;
        int currentLinkId = -1;
        while (rs.next()) {
            int rowEresourceId = rs.getInt("ERESOURCE_ID");
            int recordId = rs.getInt("RECORD_ID");
            String recordType = rs.getString("RECORD_TYPE");
            if (rowEresourceId != currentEresourceId) {
                eresource = new Eresource(null, rowEresourceId, recordId, recordType, 0, rs.getString("TITLE"));
                eresources.add(eresource);
                currentEresourceId = rowEresourceId;
            }
            int rowVersionId = rs.getInt("VERSION_ID");
            if (rowVersionId != currentVersionId) {
                version = new Version(rs.getString("DATES"), rs.getString("DESCRIPTION"), rs.getString("PUBLISHER"), rs.getString("HOLDINGS"));
                eresource.addVersion(version);
                currentVersionId = rowVersionId;
            }
            int rowLinkId = rs.getInt("LINK_ID");
            if (rowLinkId != currentLinkId) {
                version.addLink(new Link(rs.getString("INSTRUCTION"), rs.getString("LABEL"), LinkType.NORMAL, rs.getString("URL"), rs.getString("LINK_TEXT")));
                currentLinkId = rowLinkId;
            }
        }
        return eresources;
    }
}
