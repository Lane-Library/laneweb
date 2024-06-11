package edu.stanford.irt.laneweb.eresources.model.solr;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.JsonNode;

@JsonIgnoreProperties(ignoreUnknown = true, value = { "pageable" })
public class RestPage<T> extends PageImpl<T> {

    private static final long serialVersionUID = 1L;

    @JsonProperty("highlighted")
    private List<HighlightEntry<T>> highlighted = Collections.emptyList();

    public RestPage() {
        super(new ArrayList<>());
    }

    public RestPage(final List<T> content) {
        super(content);
    }

    @JsonCreator(mode = JsonCreator.Mode.PROPERTIES)
    public RestPage(@JsonProperty("content") List<T> content,
            @JsonProperty("number") int number,
            @JsonProperty("size") int size,
            @JsonProperty("totalElements") Long totalElements,
            @JsonProperty("pageable") JsonNode pageable,
            @JsonProperty("last") boolean last,
            @JsonProperty("totalPages") int totalPages,
            @JsonProperty("sort") JsonNode sort,
            @JsonProperty("first") boolean first,
            @JsonProperty("numberOfElements") int numberOfElements) {
        super(content, PageRequest.of(number, size), totalElements);
    }

    public RestPage(final List<T> content, final Pageable pageable, final long total) {
        super(content, pageable, total);
    }

    @Override
    public List<T> getContent() {
        if (null != this.highlighted) {
            return this.highlighted.stream().map(h -> h.getEntity()).toList();
        }
        return Collections.emptyList();
    }

    public List<HighlightEntry<T>> getHighlighted() {
        return this.highlighted;
    }

    public void setHighlighted(final List<HighlightEntry<T>> highlighted) {
        this.highlighted = highlighted;
    }
}