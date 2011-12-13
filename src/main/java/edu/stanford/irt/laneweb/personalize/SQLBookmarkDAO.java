package edu.stanford.irt.laneweb.personalize;

import javax.sql.DataSource;

public class SQLBookmarkDAO extends AbstractSQLBookmarkDAO<Bookmark> {

    private static final String DELETE_BOOKMARKS_SQL = "DELETE FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String READ_BOOKMARKS_SQL = "SELECT BOOKMARKS FROM BOOKMARKS WHERE SUNETID = ?";

    private static final String WRITE_BOOKMARKS_SQL =
            "BEGIN " +
            "  INSERT INTO bookmarks(sunetid, bookmarks) " +
            "  VALUES (?, empty_blob()) " +
            "  RETURN bookmarks INTO ?; " +
            "END;";

    public SQLBookmarkDAO(final DataSource dataSource) {
        super(dataSource);
    }

    @Override
    protected String getDeleteSQL() {
        return DELETE_BOOKMARKS_SQL;
    }

    @Override
    protected String getReadSQL() {
        return READ_BOOKMARKS_SQL;
    }

    @Override
    protected String getWriteSQL() {
        return WRITE_BOOKMARKS_SQL;
    }
}
