package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.stanford.irt.laneweb.resource.AbstractScoreStrategy;

public class ScoreStrategy extends AbstractScoreStrategy {

    public int computeScore(final String query, final String title, final ResultSet rs) throws SQLException {
        if (query.equalsIgnoreCase(title)) {
            return Integer.MAX_VALUE;
        } else if (title.indexOf('(') > -1 && query.equalsIgnoreCase(title.replaceFirst(" \\(.*", ""))) {
            return Integer.MAX_VALUE;
        } else {
            // core material weighted * 3
            int coreFactor = "Y".equals(rs.getString("CORE")) ? 3 : 1;
            // weighted oracle text scores for title and text
            // averaged
            int scoreFactor = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
            int year = rs.getInt("YEAR");
            // subtract number of years difference from current year
            // yearFactor can change score from -10 to 10 points
            int yearFactor = computeDateAdjustment(year);
            return scoreFactor + yearFactor;
        }
    }
}
