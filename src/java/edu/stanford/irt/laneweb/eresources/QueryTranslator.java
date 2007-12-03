//stolen from oracle web site.
package edu.stanford.irt.laneweb.eresources;

import java.util.Vector;

class WordData {

    String text;

    // String fieldName;
}

public class QueryTranslator {

    Vector<WordData> reqWords = new Vector<WordData>();

    // Vector<WordData> optWords = new Vector<WordData>();

    Vector<WordData> notWords = new Vector<WordData>();

    public static final int REQUIRED = 1;

    // public static final int optional = 2;

    public static final int NOT_WANTED = 3;

    public String translate(final String input) {
        // CY: leading space broke this sucker
        processString(input.trim());
        return getQuery();
    }

    private void addWord(final String word, final int wordType) {

        WordData wd = new WordData();

        wd.text = word;
        // wd.fieldName = field;

        switch (wordType) {
        case REQUIRED:
            this.reqWords.addElement(wd);
            break;
        // case optional:
        // optWords.addElement(wd);
        // break;
        case NOT_WANTED:
            this.notWords.addElement(wd);
            break;
        }
    }

    public void processString(final String input) {
        int p = 0;
        int startWord;
        int flag;
        String theWord;
        // String fieldName;

        this.reqWords = new Vector<WordData>();
        // optWords = new Vector<WordData>();
        this.notWords = new Vector<WordData>();

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
                flag = REQUIRED;
                // fieldName = "";

                if (theWord.charAt(0) == '+' && theWord.length() > 1) {
                    flag = REQUIRED;
                    theWord = theWord.substring(1);
                }

                else if (theWord.charAt(0) == '-' && theWord.length() > 1) {
                    flag = NOT_WANTED;
                    theWord = theWord.substring(1);
                }

                // Replace * wild cards with %

                theWord = theWord.replace('*', '%');

                addWord(theWord, flag);

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

    private String getWord(final Vector<WordData> words, final int pos) {
        // here I added stuff for handling the wildcard, which doesn't work if
        // in {}
        String word = words.elementAt(pos).text;
        String ts = word.indexOf('%') > -1 ? word : "{" + word + "}";
        // String ts = "{" + ((WordData) words.elementAt(pos)).text + "}";
        // not using fieldName at the moment
        // if (((WordData) words.elementAt(pos)).fieldName.length() > 0) {
        // ts += " WITHIN " + ((WordData) words.elementAt(pos)).fieldName;
        // }
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
        String tempString = "";

        String boolOp = ""; // AND, OR, NOT operator
        int reqCount; // Count of required words
        // int optCount; // Count of optional words
        int notCount; // Count of not wanted words
        int i; // Loop control

        boolOp = "";
        reqCount = this.reqWords.size();
        // optCount = optWords.size();
        notCount = this.notWords.size();

        if (this.reqWords.size() > 0) {
            // Required words - first time

            tempString = "((";
            for (i = 0; i < reqCount; i++) {
                tempString += boolOp + getWord(this.reqWords, i);
                boolOp = " & ";
            }

            // if (reqCount > 0 && optCount > 0) {
            // tempString += ") | ";
            // tempString += "((";
            // // Required words - second time (anded with optional words)
            // boolOp = "";
            // for (i = 0; i < reqCount; i++) {
            // tempString += boolOp + getWord(reqWords, i);
            // boolOp = " & ";
            // }
            // tempString += ")*10*10";
            //
            // tempString += " & (";
            //
            // // Required words - third time as part of accumulate
            // boolOp = "";
            // for (i = 0; i < reqCount; i++) {
            // tempString += boolOp + getWord(reqWords, i);
            // // tempString += "*2";// Uncomment to double weight of
            // // required words
            // boolOp = " , ";
            // }
            // }
        } // else
        // tempString = "(";

        // Optional words
        // Don't reset boolOp
        // for (i = 0; i < optCount; i++) {
        // tempString += boolOp + getWord(optWords, i);
        // boolOp = " , "; // Accumulate
        // }

        if (reqCount > 0) {
            // if (optCount > 0)
            // tempString += ")) )";
            // else
            tempString += ")) ";
            // else
            // tempString += ")";
        }

        // if (tempString.length() > 0)
        if (notCount > 0) {
            boolOp = " NOT ";
        } else {
            boolOp = "";
        }

        for (i = 0; i < notCount; i++) {
            tempString += boolOp + getWord(this.notWords, i);
            boolOp = " NOT ";
        }
        return tempString;
    }

    // public static void main(String[] args) {
    // System.out.println(new
    // QueryTranslator().translate(java.net.URLDecoder.decode("-tope")));
    // System.out.println(new
    // QueryTranslator().translate(java.net.URLDecoder.decode("+J.+Thorac.+Cardiovasc.+Surg.%2C+June+1%2C+2004%3B+127%286%29%3A+1858+-+1858.")));
    // }
}
