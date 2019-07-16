package edu.stanford.irt.laneweb.eresources.browse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.regex.Pattern;

import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.FacetOptions.FacetSort;
import org.springframework.data.solr.core.query.result.FacetFieldEntry;
import org.springframework.data.solr.core.query.result.FacetPage;

import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.cocoon.xml.XMLConsumer;
import edu.stanford.irt.laneweb.eresources.Eresource;
import edu.stanford.irt.laneweb.eresources.SolrService;

public class AtoZBrowseGenerator extends AbstractBrowseGenerator {

    private static final Pattern ACCEPTABLE_LETTERS = Pattern.compile("[1a-z]");

    // number of facet values to return from Solr: must be large enough to fetch all browse letters
    private static final int MAX_FACETS = 200;

    private static final String PREFIX = "ertlsw";

    private SAXStrategy<List<BrowseLetter>> saxStrategy;

    public AtoZBrowseGenerator(final String componentType, final SolrService service,
            final SAXStrategy<List<BrowseLetter>> saxStrategy) {
        super(componentType, service);
        this.saxStrategy = saxStrategy;
    }

    @Override
    protected void doGenerate(final XMLConsumer xmlConsumer) {
        if (null != this.browseType) {
            FacetPage<Eresource> fps = this.solrService.facetByField(
                    "advanced:true recordType:bib AND (isRecent:1 OR isLaneConnex:1)",
                    "type:\"" + this.browseType + '"', "title_starts", 0, MAX_FACETS, 0, FacetSort.INDEX);
            List<BrowseLetter> letters = extractFacets(fps.getFacetResultPages());
            this.saxStrategy.toSAX(letters, xmlConsumer);
        }
    }

    private List<BrowseLetter> extractFacets(final Collection<Page<FacetFieldEntry>> facetResultPages) {
        List<BrowseLetter> letters = new ArrayList<>();
        for (Page<FacetFieldEntry> page : facetResultPages) {
            for (FacetFieldEntry entry : page) {
                String entryValue = entry.getValue();
                String letter = null;
                if (entryValue.startsWith(PREFIX) && entryValue.length() == PREFIX.length() + 1) {
                    letter = entryValue.substring(PREFIX.length());
                }
                if (null != letter && ACCEPTABLE_LETTERS.matcher(letter).matches()) {
                    letters.add(new BrowseLetter(this.basePath, letter, (int) entry.getValueCount()));
                }
            }
        }
        return letters;
    }
}
