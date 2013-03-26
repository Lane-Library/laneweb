package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;

import edu.stanford.irt.laneweb.resource.AbstractScoreStrategy;

public class ScoreStrategy extends AbstractScoreStrategy {

    private static int MAX_INT_MINUS_100 = Integer.MAX_VALUE - 100;

    public int computeScore(final String query, final String title, final ResultSet rs) throws SQLException {
        int score = 0;
        int year = rs.getInt("YEAR");
        if (query.equalsIgnoreCase(title)) {
            score = MAX_INT_MINUS_100;
        } else if (title.indexOf('(') > -1 && query.equalsIgnoreCase(title.replaceFirst(" \\(.*", ""))) {
            score = MAX_INT_MINUS_100;
        } else {
            // core material weighted * 3
            int coreFactor = "Y".equals(rs.getString("CORE")) ? 3 : 1;
            // weighted oracle text scores for title and text
            // averaged
            score = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
            // subtract number of years difference from current year
            // yearFactor can change score from -10 to 10 points
        }
        score = score + computeDateAdjustment(year);
        return score;
    }
}
