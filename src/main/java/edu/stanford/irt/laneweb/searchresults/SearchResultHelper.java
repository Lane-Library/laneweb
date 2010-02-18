/**
 * 
 */
package edu.stanford.irt.laneweb.searchresults;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.cocoon.xml.XMLUtils;
import org.xml.sax.ContentHandler;
import org.xml.sax.SAXException;

/**
 * @author ryanmax
 */
public class SearchResultHelper {

    public static final String EMPTY = "";

    public static final Pattern NON_FILING_PATTERN = Pattern.compile("(?i)^(a|an|the) ");

    private static final Pattern HYPHEN_PATTERN = Pattern.compile("\\-");

    private static final Pattern UNACCEPTABLE_CHARS_PATTERN = Pattern.compile("[^a-zA-Z0-9,-_ ]");

    private static final Pattern INVERT_COMMAS_PATTERN = Pattern.compile("(\\(?((\\w| |-|_)+), ((\\w| |-|_)+)\\)?)");

    private static final String INVERT_REPLACEMENT = "$1 and ($4 $2)";

    private static final String PERIOD = "\\.";

    public static final String KEYWORD = "keyword";

    public static final String NAMESPACE = "http://lane.stanford.edu/search-results/1.0";

    /**
     * normalize query terms for use in regex pattern, where normal means:
     * 
     * <pre>
     *  lower-case
     *  invert comma separated terms: Heparin, Low-Molecular-Weight becomes Low-Molecular-Weight Heparin
     *  replace hyphens with "."
     *  strip [^a-zA-Z0-9,-_ ]
     * </pre>
     * 
     * @param query
     * @return String to use in regExp pattern
     */
    public static String regexifyQuery(String query) {
        String normalQuery;
        normalQuery = query.toLowerCase();
        normalQuery = INVERT_COMMAS_PATTERN.matcher(normalQuery).replaceAll(INVERT_REPLACEMENT);
        normalQuery = HYPHEN_PATTERN.matcher(normalQuery).replaceAll(PERIOD);
        normalQuery = UNACCEPTABLE_CHARS_PATTERN.matcher(normalQuery).replaceAll(EMPTY);
        return normalQuery.replaceAll(" and ", "|");
    }

    /**
     * handles elements without child nodes
     * 
     * @param handler
     *            the ContentHandler
     * @param name
     *            the element tagname
     * @param content
     *            the String content
     * @throws SAXException
     */
    public static void handleElement(final ContentHandler handler, final String name, final String content)
            throws SAXException {
        if ((content == null) || (content.length() == 0)) {
            return;
        }
        XMLUtils.startElement(handler, NAMESPACE, name);
        XMLUtils.data(handler, content);
        XMLUtils.endElement(handler, name);
    }

    /**
     * handles elements without child nodes
     * 
     * @param handler
     *            the ContentHandler
     * @param name
     *            the element tagname
     * @param content
     *            the String content
     * @param queryTermPattern
     *            pattern to do keyword highlighting
     * @throws SAXException
     */
    public static void handleHighlightedElement(final ContentHandler handler, final String name, final String content) throws SAXException {
        if ((content == null) || (content.length() == 0)) {
            return;
        }
        handleElement(handler, name, content);
    }
}
