package edu.stanford.irt.laneweb.eresources.browse;

public class TitleNormalizer {

    private static final String[] NO_CAP_WORDS = { "a", "and", "as", "at", "but", "by", "for", "from", "if", "in",
            "into", "like", "nor", "of", "off", "on", "once", "onto", "or", "over", "so", "than", "that", "the", "to",
            "upon", "when", "with", "yet" };

    public static String toTitleCase(final String string) {
        // e-Anatomy ... maybe others? worth a special case?
        if (string.matches("^[a-zA-Z]-.*")) {
            return string;
        }
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = true;
        for (int i = 0; i < string.length(); i++) {
            char c = string.charAt(i);
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
                continue;
            }
            if (capitalizeNext) {
                sb.append(Character.toTitleCase(c));
                capitalizeNext = false;
            } else {
                sb.append(c);
            }
        }
        String titleCased = sb.toString();
        for (String preposition : NO_CAP_WORDS) {
            titleCased = titleCased.replaceAll("(?i)(?<!^)\\b" + preposition + "\\b", preposition);
        }
        return titleCased;
    }

    private TitleNormalizer() {
        // private constructor
    }
}
