package edu.stanford.irt.laneweb.eresources;

import java.util.List;
import java.util.stream.Collectors;

public final class SolrQueryParser {

    private List<QueryInspector> combinables;

    private List<QueryInspector> nonCombinables;

    public SolrQueryParser(final List<QueryInspector> parsers) {
        this.combinables = parsers.stream()
                .filter(QueryInspector::combinable)
                .collect(Collectors.toList());
        this.nonCombinables = parsers.stream()
                .filter((final QueryInspector qi) -> !qi.combinable())
                .collect(Collectors.toList());
    }

    public String parse(final String query) {
        String parsedQuery = query;
        for (QueryInspector parser : this.nonCombinables) {
            parsedQuery = parser.inspect(query);
            if (!query.equals(parsedQuery)) {
                return parsedQuery;
            }
        }
        for (QueryInspector parser : this.combinables) {
            parsedQuery = parser.inspect(parsedQuery);
        }
        return parsedQuery;
    }
}
