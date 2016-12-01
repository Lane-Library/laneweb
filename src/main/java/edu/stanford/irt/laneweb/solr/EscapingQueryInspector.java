package edu.stanford.irt.laneweb.solr;

import java.util.HashSet;
import java.util.Set;

/**
 * Clean query strings before sending to Solr
 * <p>
 * Lucence special characters: + - &amp;&amp; || ! ( ) { } [ ] ^ " ~ * ? : \
 * https://lucene.apache.org/core/2_9_4/queryparsersyntax.html#Escaping%20Special%20Characters
 * </p>
 *
 * @author ryanmax
 */
public final class EscapingQueryInspector implements QueryInspector {

    private static final Set<Character> ESCAPEABLE_CHARS = new HashSet<>();

    static {
        // these seem harmless | &
        // these seem useful and harmless " * ( )
        ESCAPEABLE_CHARS.add(Character.valueOf('+'));
        ESCAPEABLE_CHARS.add(Character.valueOf('-'));
        ESCAPEABLE_CHARS.add(Character.valueOf('!'));
        ESCAPEABLE_CHARS.add(Character.valueOf('{'));
        ESCAPEABLE_CHARS.add(Character.valueOf('}'));
        ESCAPEABLE_CHARS.add(Character.valueOf('['));
        ESCAPEABLE_CHARS.add(Character.valueOf(']'));
        ESCAPEABLE_CHARS.add(Character.valueOf('^'));
        ESCAPEABLE_CHARS.add(Character.valueOf('~'));
        ESCAPEABLE_CHARS.add(Character.valueOf('?'));
        ESCAPEABLE_CHARS.add(Character.valueOf(':'));
        ESCAPEABLE_CHARS.add(Character.valueOf('\\'));
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

    @Override
    public String inspect(final String query) {
        return maybeEscape(query);
    }
}