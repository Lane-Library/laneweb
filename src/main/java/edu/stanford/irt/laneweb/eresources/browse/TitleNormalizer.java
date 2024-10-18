package edu.stanford.irt.laneweb.eresources.browse;

public class TitleNormalizer {

    private static final String[] NO_CAP_WORDS = { "a", "and", "as", "at", "but", "by", "for", "from", "if", "in",
            "into", "like", "nor", "of", "off", "on", "once", "onto", "or", "over", "so", "than", "that", "the", "to",
            "upon", "when", "with", "yet" };

    public static String toTitleCase(final String string) {
        if (string == null || string.trim().isEmpty()) {
            return string;
        }
        String title = string.trim();
        if (title.endsWith(".")) {
            title = title.substring(0, title.length() - 1);
        }
        // e-Anatomy ... maybe others? worth a special case?
        if (title.matches("^[a-zA-Z]-.*")) {
            return title;
        }
        StringBuilder sb = new StringBuilder();
        boolean capitalizeNext = true;
        for (char c : title.toCharArray()) {
            if (Character.isWhitespace(c)) {
                capitalizeNext = true;
                sb.append(c);
            } else if (capitalizeNext) {
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
