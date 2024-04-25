package edu.stanford.irt.laneweb.eresources.model.solr;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

public class HighlightEntry<T> implements Serializable {

    /**
     * Highlight holds reference to the field highlighting was applied to, as well as the snipplets
     *
     * @author Christoph Strobl
     */
    public static class Highlight {

        private final Field field;

        private final List<String> snipplets;

        Highlight() {
            this.field = null;
            this.snipplets = null;
        }

        /**
         * @param field
         *            must not be null
         * @param snipplets
         */
        Highlight(final Field field, @Nullable final List<String> snipplets) {
            Assert.notNull(field, "Field must not be null!");
            this.field = field;
            this.snipplets = snipplets != null ? snipplets : Collections.emptyList();
        }

        Highlight(final String fieldname, final List<String> snipplets) {
            this(new Field(fieldname), snipplets);
        }

        /**
         * @return
         */
        public Field getField() {
            return this.field;
        }

        /**
         * @return empty list none available
         */
        public List<String> getSnipplets() {
            return this.snipplets;
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
     * @param field
     * @param snipplets
     */
    public void addSnipplets(final Field field, final List<String> snipplets) {
        this.highlights.add(new Highlight(field, snipplets));
    }

    /**
     * @param fieldname
     * @param snipplets
     */
    public void addSnipplets(final String fieldname, final List<String> snipplets) {
        addSnipplets(new Field(fieldname), snipplets);
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
