package edu.stanford.irt.laneweb.eresources;

import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Escape parentheses from search terms for improved recall (LANEWEB-10809). Preserve parentheses when meaningful, like
 * in boolean searches.
 */
public final class ParenthesesQueryInspector implements QueryInspector {

    private static final Pattern BOOLEAN_PATTERN = Pattern.compile("\\b(AND|OR|NOT)\\b");

    private static final Set<Character> PARENS = new HashSet<>();
    static {
        PARENS.add(Character.valueOf('('));
        PARENS.add(Character.valueOf(')'));
    }

    private static boolean isParen(final char c) {
        return PARENS.contains(Character.valueOf(c));
    }

    private static String maybeReplaceWithSpace(final String query) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < query.length(); i++) {
            char c = query.charAt(i);
            if (isParen(c) && !BOOLEAN_PATTERN.matcher(query).find()) {
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
        return maybeReplaceWithSpace(query);
    }
}
