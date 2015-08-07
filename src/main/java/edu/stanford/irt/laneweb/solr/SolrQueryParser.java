package edu.stanford.irt.laneweb.solr;

import java.util.ArrayList;
import java.util.List;

/**
 * Clean query strings before sending to Solr
 * <p>
 * Lucence special characters: + - && || ! ( ) { } [ ] ^ " ~ * ? : \
 * https://lucene.apache.org/core/2_9_4/queryparsersyntax.html#Escaping%20Special%20Characters
 * </p>
 *
 * @author ryanmax
 */
public final class SolrQueryParser {

    private static final List<Character> ESCAPEABLE_CHARS = new ArrayList<Character>();

    private static final String TOGGLE_OFF = "advanced:true";
    static {
        // these seem harmless || &&
        // these seem useful and harmless " *
        ESCAPEABLE_CHARS.add(new Character('+'));
        ESCAPEABLE_CHARS.add(new Character('-'));
        ESCAPEABLE_CHARS.add(new Character('!'));
        ESCAPEABLE_CHARS.add(new Character('('));
        ESCAPEABLE_CHARS.add(new Character(')'));
        ESCAPEABLE_CHARS.add(new Character('{'));
        ESCAPEABLE_CHARS.add(new Character('}'));
        ESCAPEABLE_CHARS.add(new Character('['));
        ESCAPEABLE_CHARS.add(new Character(']'));
        ESCAPEABLE_CHARS.add(new Character('^'));
        ESCAPEABLE_CHARS.add(new Character('~'));
        ESCAPEABLE_CHARS.add(new Character('?'));
        ESCAPEABLE_CHARS.add(new Character(':'));
        ESCAPEABLE_CHARS.add(new Character('\\'));
    }

    // to escape: ? [ ] { }
    // to disable all escaping: advanced:true
    public static String parse(final String query) {
        if (query.contains(TOGGLE_OFF)) {
            return query.replace(TOGGLE_OFF, "").trim();
        }
        return maybeEscape(query);
    }

    private static boolean isEscapableCharacter(final char c) {
        return ESCAPEABLE_CHARS.contains(Character.valueOf(c));
    }

    private static String maybeEscape(final String query) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            if (isEscapableCharacter(c)) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }
}
