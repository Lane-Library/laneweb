package edu.stanford.irt.laneweb.eresources.browse;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.EresourceBrowseService;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetFieldEntry;
import edu.stanford.irt.laneweb.eresources.model.solr.FacetSort;

public class AtoZBrowseGenerator extends AbstractBrowseGenerator {

    private static final Pattern ACCEPTABLE_LETTERS = Pattern.compile("[1a-z]");

    // number of facet values to return from Solr: must be large enough to fetch all browse letters
    private static final int MAX_FACETS = 200;

    private static final String PREFIX = "ertlsw";

    private SAXStrategy<List<BrowseLetter>> saxStrategy;

    public AtoZBrowseGenerator(final String componentType, final EresourceBrowseService service,
            final SAXStrategy<List<BrowseLetter>> saxStrategy) {
        super(componentType, service);
        this.saxStrategy = saxStrategy;
    }

    private List<BrowseLetter> extractFacets(final Map<String, List<FacetFieldEntry>> facetResultPages) {
        List<BrowseLetter> letters = new ArrayList<>();
        for (List<FacetFieldEntry> page : facetResultPages.values()) {
            for (FacetFieldEntry entry : page) {
                String entryValue = entry.getValue();
                String letter = null;
                if (entryValue.startsWith(PREFIX) && entryValue.length() == PREFIX.length() + 1) {
                    letter = entryValue.substring(PREFIX.length());
                }
                if (null != letter && ACCEPTABLE_LETTERS.matcher(letter).matches()) {
                    letters.add(new BrowseLetter(this.basePath, letter, entry.getValueCount()));
                }
            }
        }
        return letters;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null != this.browseQuery) {
            Map<String, List<FacetFieldEntry>> fps = this.restBrowseService.facetByField(BASE_BROWSE_QUERY,
                    this.browseQuery, "title_starts", 0, MAX_FACETS, 0, FacetSort.INDEX);
            List<BrowseLetter> letters = extractFacets(fps);
            this.saxStrategy.toSAX(letters, xmlConsumer);
        }
    }
}
