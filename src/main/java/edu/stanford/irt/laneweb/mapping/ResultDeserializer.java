package edu.stanford.irt.laneweb.mapping;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;

import org.openqa.selenium.json.Json;

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
import edu.stanford.irt.search.impl.ContentResult.ContentResultBuilder;

public class ResultDeserializer extends JsonDeserializer<Result> {

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

    private static Result getEngines(final JsonNode jsonNode) {
        Collection<Result> results = new ArrayList<>();
        Query query = getQuery(jsonNode.get(QUERY_NODE_NAME));
        JsonNode enginesNode = jsonNode.get(ENGINES_NODE_NAME);
        for (JsonNode engineNode : enginesNode) {
            String executionTime = String.valueOf(engineNode.get(TIME_NODE_NAME).asInt(-1));
            ResultBuilder builder = Result.newResultBuilder().id(engineNode.get(ENGINE_ID_NODE_NAME).textValue())
                    .description(engineNode.get(ENGINE_NAME_NODE_NAME).textValue()).query(query)
                    .status(getStatus(engineNode.get(STATUS_NODE_NAME))).time(executionTime)
                    .url(engineNode.get(REQUEST_URL_NODE_NAME).textValue())
                    .hits(engineNode.get(TOTAL_HITS_NODE_NAME).asLong(-1))
                    .children(getResources(engineNode.get(RESOURCES_NODE_NAME), query, executionTime));
            results.add(builder.build());
        }

        Result searchResult = Result.newResultBuilder().id(jsonNode.get(SEARCH_ID_NODE_NAME).asText())
                .description(jsonNode.get(SERVICE_INFO_NODE_NAME).asText()).query(query)
                .status(getStatus(jsonNode.get(STATUS_NODE_NAME))).children(results).build();
        return searchResult;
    }

    private static Collection<Result> getResources(final JsonNode nodes, final Query query,
            String parentExecutionTime) {
        Collection<Result> results = new ArrayList<>();
        for (JsonNode node : nodes) {
            ResultBuilder builder = Result.newResultBuilder().description(node.get(RESOURCE_NAME_NODE_NAME).textValue())
                    .id(node.get(RESOURCE_ID_NODE_NAME).textValue()).query(query)
                    .status(getStatus(node.get(STATUS_NODE_NAME))).time(parentExecutionTime)
                    .url(node.get(RESOURCE_URL_NODE_NAME).textValue())
                    .children(getContentResultFromNode(node.get(RESULTS_NODE_NAME)));
            long hits = node.get(RESOURCE_HITS_NODE_NAME).asLong();
            if (hits != -1) {
                builder.hits(hits);
            }
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
                    .author(node.get(CONTENT_AUTHOR_NODE_NAME).asText(null))
                    .contentId(node.get(CONTENT_ID_NODE_NAME).asText(null))
                    .url(node.get(RESOURCE_URL_NODE_NAME).asText(null))
                    .description(node.get(CONTENT_DESCRIPTION_NODE_NAME).asText(null))
                    .title(node.get(CONTENT_TITLE_NODE_NAME).asText(null)).id("content-" + idx++);
            if (!publicationNode.isNull()) {
                contentResultBuilder.date(publicationNode.get(CONTENT_PUBLICATION_DATE_NODE_NAME).asText(null))
                        .issue(publicationNode.get(CONTENT_PUBLICATION_ISSUE_NODE_NAME).asText(null))
                        .pubTitle(publicationNode.get(CONTENT_PUBLICATION_TITLE_NODE_NAME).asText(null))
                        .volume(publicationNode.get(CONTENT_PUBLICATION_VOLUME_NODE_NAME).asText(null));
            }
            results.add(contentResultBuilder.build());
        }
        return results;
    }

    private static Query getQuery(final JsonNode node) {
        Query query = null;
        if (!node.isNull()) {
            query = new SimpleQuery(node.textValue());
        }
        return query;
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
        return getEngines(p.getCodec().readTree(p));
    }
}
