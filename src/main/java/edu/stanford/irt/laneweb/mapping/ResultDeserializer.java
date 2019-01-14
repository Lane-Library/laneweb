package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.Result.ResultBuilder;
import edu.stanford.irt.search.impl.SimpleQuery;

public class ResultDeserializer extends JsonDeserializer<Result> {

    private static final int CONTENT_DEPTH = 3;

    private static Collection<Result> getChildren(final JsonNode jsonNode, int depth) {
        Collection<Result> children = new ArrayList<>();
        jsonNode.forEach((final JsonNode n) -> children.add(getResultFromNode(n, depth + 1)));
        return children;
    }

    private static Result getContentResultFromNode(final JsonNode node) {
        return ContentResult.newContentResultBuilder()
                .author(node.get("author").asText(null))
                .contentId(node.get("contentId").asText(null))
                .date(node.get("publicationDate").asText(null))
                .description(node.get("description").asText(null))
                .id(node.get("id").asText(null))
                .issue(node.get("publicationIssue").asText(null))
                .pubTitle(node.get("publicationTitle").asText(null))
                .title(node.get("title").asText(null))
                .url(node.get("url").asText(null))
                .volume(node.get("publicationVolume").asText(null))
                .build();
    }

    private static Result getPlainResultFromNode(final JsonNode node, int depth) {
        ResultBuilder builder = Result.newResultBuilder()
                .children(getChildren(node.get("children"), depth))
                .description(node.get("description").textValue())
                .id(node.get("id").textValue())
                .query(getQuery(node.get("query")))
                .status(getStatus(node.get("status")))
                .time(node.get("time").asText())
                .url(node.get("url").textValue());
        String hits = node.get("hits").textValue();
        if (hits != null) {
            builder.hits(hits);
        }
        return builder.build();
    }

    private static Query getQuery(final JsonNode node) {
        Query query = null;
        if (!node.isNull()) {
            query = new SimpleQuery(node.textValue());
        }
        return query;
    }

    private static Result getResultFromNode(final JsonNode node, int depth) {
        if (depth == CONTENT_DEPTH) {
            return getContentResultFromNode(node);
        } else {
            return getPlainResultFromNode(node, depth);
        }
    }

    private static SearchStatus getStatus(final JsonNode node) {
        SearchStatus status = null;
        if (!node.isNull()) {
            status = SearchStatus.valueOf(node.asText());
        }
        return status;
    }

    @Override
    public Result deserialize(final JsonParser p, final DeserializationContext ctxt) throws IOException {
        return getResultFromNode(p.getCodec().readTree(p), 0);
    }
}
