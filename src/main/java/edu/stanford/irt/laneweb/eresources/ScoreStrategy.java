package edu.stanford.irt.laneweb.eresources;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.resource.AbstractScoreStrategy;

public class ScoreStrategy extends AbstractScoreStrategy {

    private static final int MAX_INT_MINUS_100 = Integer.MAX_VALUE - 100;
    
    private static final Pattern PATTERN = Pattern.compile(" \\(.*");

    public int computeScore(final String query, final String title, final ResultSet rs) throws SQLException {
        int score = 0;
        int year = rs.getInt("YEAR");
        if (query.equalsIgnoreCase(title)) {
            score = MAX_INT_MINUS_100;
        } else if (title.indexOf('(') > -1 && query.equalsIgnoreCase(PATTERN.matcher(title).replaceFirst(""))) {
            score = MAX_INT_MINUS_100;
        } else {
            // core material weighted * 3
            int coreFactor = "Y".equals(rs.getString("CORE")) ? 3 : 1;
            // weighted oracle text scores for title and text
            // averaged
            score = ((rs.getInt("SCORE_TITLE") * coreFactor) + (rs.getInt("SCORE_TEXT") * coreFactor)) / 2;
        }
        score = score + computeDateAdjustment(year);
        return score;
    }
}
