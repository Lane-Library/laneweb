package edu.stanford.irt.laneweb.servlet.mvc;

import edu.stanford.irt.spell.SpellCheckResult;

public class SpellingResult extends SpellCheckResult {

    public static final SpellingResult NULL_SPELLING_RESULT = new SpellingResult(null);

    protected int suggestionResultCount;

    public SpellingResult(final String suggestion) {
        super(suggestion);
    }

    public int getSuggestionResultCount() {
        return this.suggestionResultCount;
    }
}
