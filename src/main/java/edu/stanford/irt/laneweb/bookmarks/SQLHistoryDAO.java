package edu.stanford.irt.laneweb.bookmarks;

import javax.sql.DataSource;

public class SQLHistoryDAO extends AbstractSQLBookmarkDAO<History> {

    private static final String DELETE_HISTORY_SQL = "DELETE FROM HISTORY WHERE SUNETID = ?";

    private static final String READ_HISTORY_SQL = "SELECT HISTORY FROM HISTORY WHERE SUNETID = ?";

    private static final String WRITE_HISTORY_SQL = 
            "BEGIN " +
            "  INSERT INTO history(sunetid, history) " +
            "  VALUES (?, empty_blob()) " +
            "  RETURN history INTO ?; " +
            "END;";

    public SQLHistoryDAO(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getDeleteSQL() {
        return DELETE_HISTORY_SQL;
    }

    @Override
    protected String getReadSQL() {
        return READ_HISTORY_SQL;
    }

    @Override
    protected String getWriteSQL() {
        return WRITE_HISTORY_SQL;
    }
}
