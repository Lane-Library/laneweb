package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class HighlightEntry<T> implements Serializable {

    /**
     * Highlight holds reference to the field highlighting was applied to, as well as the snippets
     *
     * @author Christoph Strobl
     */
    public static class Highlight {

        private final String fieldName;

        private final List<String> snippets;

        Highlight() {
            this.fieldName = null;
            this.snippets = null;
        }

        /**
         * @param fieldName
         *            must not be null
         * @param snippets
         */
        Highlight(final String fieldName, @Nullable final List<String> snippets) {
            Assert.notNull(fieldName, "fieldName must not be null!");
            this.fieldName = fieldName;
            this.snippets = snippets != null ? snippets : Collections.emptyList();
        }

        /**
         * @return
         */
        public String getFieldName() {
            return this.fieldName;
        }

        /**
         * @return empty list none available
         */
        public List<String> getSnippets() {
            return this.snippets;
        }
    }

    private static final long serialVersionUID = 4449625843509893992L;

    private final T entity;

    private final List<Highlight> highlights = new ArrayList<>(1);

    public HighlightEntry() {
        this.entity = null;
    }

    /**
     * @param entity
     *            must not be null
     */
    public HighlightEntry(final T entity) {
        Assert.notNull(entity, "Entity must not be null!");
        this.entity = entity;
    }

    /**
     * @param fieldName
     * @param snippets
     */
    public void addSnippets(final String fieldName, final List<String> snippets) {
        this.highlights.add(new Highlight(fieldName, snippets));
    }

    /**
     * Get the entity the highlights are associated to
     *
     * @return
     */
    public T getEntity() {
        return this.entity;
    }

    /**
     * @return empty collection if none available
     */
    public List<Highlight> getHighlights() {
        return Collections.unmodifiableList(this.highlights);
    }
}
