package edu.stanford.irt.laneweb.config;

import java.util.HashMap;
import java.util.Map;

import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.impl.HttpSolrClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.data.domain.Page;
import org.springframework.data.solr.core.query.result.FacetPage;
import org.springframework.data.solr.repository.config.EnableSolrRepositories;
import org.springframework.oxm.Marshaller;

import edu.stanford.irt.cocoon.pipeline.Generator;
import edu.stanford.irt.cocoon.xml.SAXStrategy;
import edu.stanford.irt.laneweb.images.BassettAccordionGenerator;
import edu.stanford.irt.laneweb.images.BassettCountSAXStrategy;
import edu.stanford.irt.laneweb.images.BassettImageGenerator;
import edu.stanford.irt.laneweb.images.BassettImageListSAXStrategy;
import edu.stanford.irt.laneweb.images.SolrAdminImageSearchGenerator;
import edu.stanford.irt.laneweb.images.SolrAdminImageSearchSAXStrategy;
import edu.stanford.irt.laneweb.images.SolrImageSearchGenerator;
import edu.stanford.irt.laneweb.images.SolrImageSearchResult;
import edu.stanford.irt.laneweb.images.SolrImageSearchSAXStrategy;
import edu.stanford.irt.laneweb.images.SolrImageSearchTabGenerator;
import edu.stanford.irt.solr.BassettImage;
import edu.stanford.irt.solr.service.SolrImageService;

@Configuration
@EnableSolrRepositories(basePackages = {
        "edu.stanford.irt.solr.repository.search" }, multicoreSupport = true, solrClientRef = "solrSearcherServer")
public class ImagesConfiguration {

    private String imageSearchURL;

    private Marshaller marshaller;

    @Autowired
    public ImagesConfiguration(@Value("${laneweb.solr-url-imageSearch}") final String imageSearchURL,
            final Marshaller marshaller) {
        this.imageSearchURL = imageSearchURL;
        this.marshaller = marshaller;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett-accordion")
    @Scope("prototype")
    public Generator bassettAccordionGenerator() {
        return new BassettAccordionGenerator(solrImageService(), countSAXStrategy());
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/bassett")
    @Scope("prototype")
    public Generator bassettGenerator() {
        return new BassettImageGenerator(solrImageService(), pageSAXStrategy());
    }

    @Bean
    public SAXStrategy<FacetPage<BassettImage>> countSAXStrategy() {
        return new BassettCountSAXStrategy();
    }

    @Bean
    public SAXStrategy<Page<BassettImage>> pageSAXStrategy() {
        return new BassettImageListSAXStrategy();
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/admin-search-image")
    @Scope("prototype")
    public Generator solrAdminImageSearchGenerator() {
        return new SolrAdminImageSearchGenerator(solrImageService(), solrAdminImageSearchSAXStrategy());
    }

    @Bean
    public SAXStrategy<SolrImageSearchResult> solrAdminImageSearchSAXStrategy() {
        SolrAdminImageSearchSAXStrategy saxStrategy = new SolrAdminImageSearchSAXStrategy();
        saxStrategy.setWebsiteIdMapping(websiteIdMapping());
        return saxStrategy;
    }

    @Bean(name = "solrSearcherServer")
    public SolrClient solrClient() {
        return new HttpSolrClient(this.imageSearchURL);
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/search-image")
    @Scope("prototype")
    public Generator solrImageSearchGenerator() {
        return new SolrImageSearchGenerator(solrImageService(), solrImageSearchSAXStrategy());
    }

    @Bean
    public SAXStrategy<SolrImageSearchResult> solrImageSearchSAXStrategy() {
        SolrImageSearchSAXStrategy saxStrategy = new SolrImageSearchSAXStrategy();
        saxStrategy.setWebsiteIdMapping(websiteIdMapping());
        return saxStrategy;
    }

    @Bean(name = "edu.stanford.irt.cocoon.pipeline.Generator/facet-copyright")
    @Scope("prototype")
    public Generator solrImageSearchTabGenerator() {
        return new SolrImageSearchTabGenerator(solrImageService(), this.marshaller);
    }

    @Bean(name = "edu.stanford.irt.solr.service")
    public SolrImageService solrImageService() {
        return new SolrImageService();
    }

    @Bean
    public Map<String, String> websiteIdMapping() {
        Map<String, String> websiteIdMapping = new HashMap<>();
        websiteIdMapping.put("Bassett", "Stanford Bassett Anatomy");
        websiteIdMapping.put("microbelibrary.org", "ASM Microbe Library");
        websiteIdMapping.put("endoatlas.com", "Atlas of GI Endoscopy");
        websiteIdMapping.put("medpics.ucsd.edu", "UCSD MedPics");
        websiteIdMapping.put("health.education.assets.library", "U Utah Health Ed Asset Lib");
        websiteIdMapping.put("usc.orthopaedic.surgical", "USC Ortho Surg Anatomy");
        websiteIdMapping.put("ncbi.nlm.nih.gov", "Pubmed Central");
        websiteIdMapping.put("wellcomeimages.org", "Wellcome Images");
        websiteIdMapping.put("visualsonline.cancer.gov", "National Cancer Institute");
        websiteIdMapping.put("commons.wikimedia.org", "Wikimedia Commons");
        websiteIdMapping.put("indiana.edu", "Indiana U X-Rays via NLM");
        return websiteIdMapping;
    }
}