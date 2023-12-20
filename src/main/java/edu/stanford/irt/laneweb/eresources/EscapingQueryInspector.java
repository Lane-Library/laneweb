package edu.stanford.irt.laneweb.eresources;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Clean query strings before sending to Solr
 * <p>
 * Lucence special characters: + - &amp;&amp; || ! ( ) { } [ ] ^ " ~ * ? : \
 * https://lucene.apache.org/core/8_9_0/queryparser/org/apache/lucene/queryparser/classic/package-summary.html#Escaping_Special_Characters
 * </p>
 *
 * @author ryanmax
 */
public final class EscapingQueryInspector implements QueryInspector {

    private static final Set<Character> ESCAPEABLE_CHARS = new HashSet<>();

    private static final Set<String> FIELDS = new HashSet<>();

    private static final Pattern NON_WORD_CHAR_PATTERN = Pattern.compile("\\W");
    static {
        // these seem harmless | &
        // these seem useful and harmless " * ~
        // although ** needs escaping
        // parentheses handled in ParenthesesQueryInspector
        ESCAPEABLE_CHARS.add(Character.valueOf('+'));
        ESCAPEABLE_CHARS.add(Character.valueOf('-'));
        ESCAPEABLE_CHARS.add(Character.valueOf('!'));
        ESCAPEABLE_CHARS.add(Character.valueOf('{'));
        ESCAPEABLE_CHARS.add(Character.valueOf('}'));
        ESCAPEABLE_CHARS.add(Character.valueOf('['));
        ESCAPEABLE_CHARS.add(Character.valueOf(']'));
        ESCAPEABLE_CHARS.add(Character.valueOf('^'));
        ESCAPEABLE_CHARS.add(Character.valueOf('?'));
        ESCAPEABLE_CHARS.add(Character.valueOf('\\'));
    }
    static {
        // experiment ... not a comprehensive list of Solr fields
        FIELDS.add("author");
        FIELDS.add("id");
        FIELDS.add("mesh");
        FIELDS.add("primaryType");
        FIELDS.add("recordId");
        FIELDS.add("recordType");
        FIELDS.add("title");
        FIELDS.add("type");
    }

    private static boolean isEscapableCharacter(final char c) {
        return ESCAPEABLE_CHARS.contains(Character.valueOf(c));
    }

    private static boolean isField(final String s) {
        int index = s.lastIndexOf(' ') + 1;
        String maybeField = NON_WORD_CHAR_PATTERN.matcher(s.substring(index)).replaceAll("");
        return FIELDS.contains(s) || FIELDS.contains(maybeField);
    }

    private static String maybeEscape(final String query) {
        StringBuilder sb = new StringBuilder();
        int length = query.length();
        for (int i = 0; i < length; i++) {
            char c = query.charAt(i);
            char n = length > i + 1 ? query.charAt(i + 1) : 0;
            char p = length > 0 && i > 0 ? query.charAt(i - 1) : 0;
            if (isEscapableCharacter(c)
                    || (':' == c && !isField(sb.toString()) || ('*' == c && '*' == n) || ('*' == c && '*' == p))) {
                sb.append('\\');
            }
            sb.append(c);
        }
        return sb.toString();
    }

    @Override
    public boolean combinable() {
        return true;
    }

    @Override
    public String inspect(final String query) {
        return maybeEscape(query);
    }
}
