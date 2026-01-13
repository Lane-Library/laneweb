package edu.stanford.irt.laneweb.mapping;

import java.util.ArrayList;
import java.util.Collection;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.ValueDeserializer;

import edu.stanford.irt.search.Query;
import edu.stanford.irt.search.SearchStatus;
import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.Result.ResultBuilder;
import edu.stanford.irt.search.impl.SimpleQuery;
import edu.stanford.irt.search.impl.ContentResult.ContentResultBuilder;

public class ResultDeserializer extends ValueDeserializer<Result> {

    private static final String SEARCH_ID_NODE_NAME = "searchId";
    private static final String SERVICE_INFO_NODE_NAME = "serviceInfo";
    private static final String ENGINES_NODE_NAME = "engines";
    private static final String RESOURCES_NODE_NAME = "resources";
    private static final String RESULTS_NODE_NAME = "results";
    private static final String QUERY_NODE_NAME = "originalQuery";
    private static final String TIME_NODE_NAME = "executionTimeMs";
    private static final String ENGINE_ID_NODE_NAME = "engineId";
    private static final String ENGINE_NAME_NODE_NAME = "engineName";
    private static final String STATUS_NODE_NAME = "status";
    private static final String REQUEST_URL_NODE_NAME = "requestUrl";
    private static final String TOTAL_HITS_NODE_NAME = "totalHits";
    private static final String RESOURCE_NAME_NODE_NAME = "resourceName";
    private static final String RESOURCE_ID_NODE_NAME = "resourceId";
    private static final String RESOURCE_URL_NODE_NAME = "retrievalUrl";
    private static final String RESOURCE_HITS_NODE_NAME = "hitCount";
    private static final String CONTENT_ID_NODE_NAME = "externalId";
    private static final String CONTENT_DESCRIPTION_NODE_NAME = "description";
    private static final String CONTENT_AUTHOR_NODE_NAME = "author";
    private static final String CONTENT_PUBLICATION_NODE_NAME = "publication";
    private static final String CONTENT_PUBLICATION_TITLE_NODE_NAME = "title";
    private static final String CONTENT_PUBLICATION_DATE_NODE_NAME = "date";
    private static final String CONTENT_PUBLICATION_VOLUME_NODE_NAME = "volume";
    private static final String CONTENT_PUBLICATION_ISSUE_NODE_NAME = "issue";
    private static final String CONTENT_TITLE_NODE_NAME = "title";

    private static String nullIfEmpty(final JsonNode node) {
        if (node == null || node.isNull()) {
            return null;
        }
        String value = node.asString();
        return value == null || value.isEmpty() ? null : value;
    }

    private static Long longMinusOneIfEmpty(final JsonNode node) {
        return node == null || node.isNull() ? -1L : node.asLong();
    }

    private static String stringMinusOneIfEmpty(final JsonNode node) {
        return node == null || node.isNull() ? "-1" : node.asString();
    }

    private static Result getEngines(final JsonNode jsonNode) {
        Collection<Result> results = new ArrayList<>();
        Query query = getQuery(jsonNode.get(QUERY_NODE_NAME));
        JsonNode enginesNode = jsonNode.get(ENGINES_NODE_NAME);
        for (JsonNode engineNode : enginesNode) {

            String executionTime = String.valueOf(engineNode.get(TIME_NODE_NAME).asInt(-1));

            ResultBuilder builder = Result.newResultBuilder().id(engineNode.get(ENGINE_ID_NODE_NAME).asString())
                    .description(engineNode.get(ENGINE_NAME_NODE_NAME).asString()).query(query)
                    .status(getStatus(engineNode.get(STATUS_NODE_NAME)))
                    .time(stringMinusOneIfEmpty(engineNode.get(TIME_NODE_NAME)))
                    .url(engineNode.get(REQUEST_URL_NODE_NAME).asString())
                    .hits(longMinusOneIfEmpty(engineNode.get(TOTAL_HITS_NODE_NAME)))
                    .children(getResources(engineNode.get(RESOURCES_NODE_NAME), query, executionTime));
            results.add(builder.build());
        }

        Result searchResult = Result.newResultBuilder().id(jsonNode.get(SEARCH_ID_NODE_NAME).asString())
                .description(jsonNode.get(SERVICE_INFO_NODE_NAME).asString()).query(query)
                .status(getStatus(jsonNode.get(STATUS_NODE_NAME))).children(results).build();
        return searchResult;
    }

    private static Collection<Result> getResources(final JsonNode nodes, final Query query,
            String parentExecutionTime) {
        Collection<Result> results = new ArrayList<>();
        for (JsonNode node : nodes) {
            ResultBuilder builder = Result.newResultBuilder().description(node.get(RESOURCE_NAME_NODE_NAME).asString())
                    .id(node.get(RESOURCE_ID_NODE_NAME).asString()).query(query)
                    .status(getStatus(node.get(STATUS_NODE_NAME))).time(parentExecutionTime)
                    .url(node.get(RESOURCE_URL_NODE_NAME).asString())
                    .children(getContentResultFromNode(node.get(RESULTS_NODE_NAME)));

            builder.hits(node.get(RESOURCE_HITS_NODE_NAME).asLong(0L));

            results.add(builder.build());
        }
        return results;
    }

    private static Collection<Result> getContentResultFromNode(final JsonNode nodes) {
        Collection<Result> results = new ArrayList<>();
        int idx = 0;
        for (JsonNode node : nodes) {
            JsonNode publicationNode = node.get(CONTENT_PUBLICATION_NODE_NAME);
            ContentResultBuilder contentResultBuilder = ContentResult.newContentResultBuilder()
                    .author(nullIfEmpty(node.get(CONTENT_AUTHOR_NODE_NAME)))
                    .contentId(nullIfEmpty(node.get(CONTENT_ID_NODE_NAME)))
                    .url(node.get(RESOURCE_URL_NODE_NAME).asString(null))
                    .description(node.get(CONTENT_DESCRIPTION_NODE_NAME).asString(null))
                    .title(node.get(CONTENT_TITLE_NODE_NAME).asString(null)).id("content-" + idx++);
            if (!publicationNode.isNull()) {
                contentResultBuilder.date(publicationNode.get(CONTENT_PUBLICATION_DATE_NODE_NAME).asString(null))
                        .issue(publicationNode.get(CONTENT_PUBLICATION_ISSUE_NODE_NAME).asString(null))
                        .pubTitle(publicationNode.get(CONTENT_PUBLICATION_TITLE_NODE_NAME).asString(null))
                        .volume(publicationNode.get(CONTENT_PUBLICATION_VOLUME_NODE_NAME).asString(null));
            }
            results.add(contentResultBuilder.build());
        }
        return results;
    }

    private static Query getQuery(final JsonNode node) {
        Query query = null;
        if (!node.isNull()) {
            query = new SimpleQuery(node.asString());
        }
        return query;
    }

    private static SearchStatus getStatus(final JsonNode node) {
        SearchStatus status = null;
        if (!node.isNull()) {
            status = SearchStatus.valueOf(node.asString());
        }
        return status;
    }

    @Override
    public Result deserialize(final JsonParser p, final DeserializationContext ctxt) throws JacksonException {
        JsonNode root = p.readValueAsTree();
        return getEngines(root);
    }
}
