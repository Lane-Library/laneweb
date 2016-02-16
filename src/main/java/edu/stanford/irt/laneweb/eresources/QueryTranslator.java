// stolen from oracle web site.
package edu.stanford.irt.laneweb.eresources;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import edu.stanford.irt.laneweb.LanewebException;

public class QueryTranslator {

    private static final Pattern CURLY = Pattern.compile("(\\{|\\})");

    private static final Pattern QUOTES = Pattern.compile("\\s+(?=([^\"]*\"[^\"]*\")*[^\"]*$)");

    private static final Pattern WILDCARD = Pattern.compile("[\\W&&[^%]]");

    private List<String> notWords = new ArrayList<>();

    private List<String> reqWords = new ArrayList<>();

    public String translate(final String theInput) {
        if (theInput == null) {
            throw new IllegalArgumentException("null input");
        }
        String input = CURLY.matcher(theInput).replaceAll("");
        processString(input);
        if (this.reqWords.isEmpty()) {
            throw new LanewebException("no 'required' words in query: " + theInput);
        }
        String translatedQuery = getQuery();
        if ((translatedQuery.indexOf("()") > -1) || (translatedQuery.indexOf("{}") > -1)
                || (translatedQuery.indexOf("\\}") > -1)) {
            throw new LanewebException("can't construct a valid oracle text query from: " + theInput);
        }
        return translatedQuery;
    }

    protected String getQuery() {
        StringBuilder sb = new StringBuilder();
        // AND, OR, NOT operator
        String boolOp = "";
        // Count of required words
        int reqCount;
        // Count of not wanted words
        int notCount;
        // Loop control
        int i;
        boolOp = "";
        reqCount = this.reqWords.size();
        notCount = this.notWords.size();
        if (!this.reqWords.isEmpty()) {
            // Required words - first time
            sb.append("((");
            for (i = 0; i < reqCount; i++) {
                sb.append(boolOp).append(getWord(this.reqWords, i));
                boolOp = " & ";
            }
        }
        if (reqCount > 0) {
            // whole phrase search: case 103309
            if (!this.reqWords.isEmpty() && sb.toString().lastIndexOf('$') > 2) {
                sb.append("|${");
                for (String word : this.reqWords) {
                    sb.append(word).append(" ");
                }
                sb.deleteCharAt(sb.length() - 1);
                sb.append("}");
            }
            sb.append(")) ");
        }
        if (notCount > 0) {
            boolOp = " NOT ";
        } else {
            boolOp = "";
        }
        for (i = 0; i < notCount; i++) {
            sb.append(boolOp).append(getWord(this.notWords, i));
            boolOp = " NOT ";
        }
        return sb.toString();
    }

    protected void processString(final String input) {
        this.reqWords = new ArrayList<>();
        this.notWords = new ArrayList<>();
        // Loop over all words
        String[] words = QUOTES.split(input);
        for (String word : words) {
            handleWord(word);
        }
    }

    // Get word gets a single word from the "words" vector,
    // surrounds it in braces (to avoid reserved words)
    // and attaches a WITHIN clause if appropriate.
    private void addWord(final String word, final boolean isRequired) {
        if (isRequired) {
            this.reqWords.add(word);
        } else {
            this.notWords.add(word);
        }
    }

    // getQuery returns a formatted, ready-to-run ConText query.
    // In order to satisfy the altavista syntax, we have to generate
    // the following query:
    // ( req1 & req2 & ... reqN)
    // | ( (req1 & req2 & .. reqN)*10*10
    // & (req1, req2 , ... reqN , opt1 , opt2 , ... optN) )
    // NOT (not1 | not2 | ... notN)
    private String getWord(final List<String> words, final int pos) {
        // here I added stuff for handling the wildcard, which doesn't work if
        // in curly braces
        String word = words.get(pos);
        if (word.indexOf('%') > -1) {
            word = WILDCARD.matcher(word).replaceAll("");
            if ("%".equals(word)) {
                return "";
            }
            return word;
        }
        if (word.lastIndexOf('\\') == word.length() - 1) {
            word = word.substring(0, word.length() - 1);
        }
        return "${".concat(word) + '}';
    }

    private void handleWord(final String theWord) {
        String word = theWord;
        // CY bug 11825, don't process zero length string
        if (word.length() > 0) {
            // CY changed this to required from optional to make it AND
            // logic
            boolean isRequired = true;
            if (word.charAt(0) == '+' && word.length() > 1) {
                isRequired = true;
                word = word.substring(1);
            } else if (word.charAt(0) == '-' && word.length() > 1) {
                isRequired = false;
                word = word.substring(1);
            }
            // Replace * wild cards with %
            word = word.replace('*', '%');
            if (!"%".equals(word)) {
                addWord(word, isRequired);
            }
        }
    }
}
