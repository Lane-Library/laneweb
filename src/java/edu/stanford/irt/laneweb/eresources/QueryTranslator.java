//stolen from oracle web site.
package edu.stanford.irt.laneweb.eresources;

import java.util.Vector;

public class QueryTranslator {

    Vector<String> reqWords = new Vector<String>();

    Vector<String> notWords = new Vector<String>();

    public String translate(final String input) {
        processString(input);
        if (this.reqWords.size() == 0 ) {
            throw new IllegalArgumentException("no 'required' words in query: " + input);
        }
        String translatedQuery = getQuery();
        if (translatedQuery.indexOf("()") > -1 || translatedQuery.indexOf("{}") > -1 || translatedQuery.indexOf("(%)") > -1) {
            throw new IllegalArgumentException("can't construct a valid oracle text query from: " + input);
        }
        return getQuery();
    }

    private void addWord(final String word, final boolean required) {
        
        if (required) {
            this.reqWords.add(word);
        } else {
            this.notWords.add(word);
        }
    }

    public void processString(final String input) {
        int p = 0;
        int startWord;
        String theWord;

        this.reqWords = new Vector<String>();
        this.notWords = new Vector<String>();

        while (true) { // Loop over all words

            startWord = p;
            while (p < input.length() && input.charAt(p) != ' ') {
                // Check for quoted phrase
                if (input.charAt(p) == '"') { // Quote - skip to next or end
                    p++; // skip the actual quote
                    while (p < input.length() && input.charAt(p) != '"') {
                        p++;
                    }
                    if (p < input.length()) {
                        p++; // Skip the final quote if found
                    }
                } else {
                    p++;
                }
            }

            // Got a word. Check for required/not wanted flags (+-)

            theWord = input.substring(startWord, p);
            // CY bug 11825, don't process zero length string
            if (theWord.length() > 0) {
                // CY changed this to required from optional to make it AND
                // logic
                boolean required = true;

                if (theWord.charAt(0) == '+' && theWord.length() > 1) {
                    required = true;
                    theWord = theWord.substring(1);
                }

                else if (theWord.charAt(0) == '-' && theWord.length() > 1) {
                    required = false;
                    theWord = theWord.substring(1);
                }

                // Replace * wild cards with %

                theWord = theWord.replace('*', '%');

                addWord(theWord, required);

            }
            p++;
            if (p >= input.length()) {
                break;
            }
        }
    }

    // Get word gets a single word from the "words" vector,
    // surrounds it in braces (to avoid reserved words)
    // and attaches a WITHIN clause if appropriate.

    private String getWord(final Vector<String> words, final int pos) {
        // here I added stuff for handling the wildcard, which doesn't work if
        // in {}
        String word = words.elementAt(pos);
        String ts = word.indexOf('%') > -1 ? word : "{" + word + "}";
        return ts;
    }

    // getQuery returns a formatted, ready-to-run ConText query.
    // In order to satisfy the altavista syntax, we have to generate
    // the following query:

    // ( req1 & req2 & ... reqN)
    // | ( (req1 & req2 & .. reqN)*10*10
    // & (req1, req2 , ... reqN , opt1 , opt2 , ... optN) )
    // NOT (not1 | not2 | ... notN)

    public String getQuery() {
        StringBuffer sb = new StringBuffer();
//        String tempString = "";

        String boolOp = ""; // AND, OR, NOT operator
        int reqCount; // Count of required words
        int notCount; // Count of not wanted words
        int i; // Loop control

        boolOp = "";
        reqCount = this.reqWords.size();
        notCount = this.notWords.size();

        if (this.reqWords.size() > 0) {
            // Required words - first time

            sb.append("((");
            for (i = 0; i < reqCount; i++) {
                sb.append(boolOp).append(getWord(this.reqWords, i));
                boolOp = " & ";
            }
        }

        if (reqCount > 0) {
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
}
