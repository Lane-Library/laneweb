package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = edu.stanford.irt.laneweb.config.LanewebConfiguration.class)
public class LanewebIT {

    private static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript",
            StandardCharsets.UTF_8);

    private static final MediaType APPLICATION_JSON = new MediaType("application", "json", StandardCharsets.UTF_8);

    private static final MediaType IMAGE_PNG = new MediaType("image", "png");

    private static final MediaType TEXT_HTML = new MediaType("text", "html", StandardCharsets.UTF_8);

    private static final MediaType TEXT_XML = new MediaType("text", "xml", StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupFixture() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testBioresearchBrowse() throws Exception {
        this.mockMvc.perform(get("/biomed-resources/ejsubjectbrowse.html?m=Ophthalmology").servletPath("/biomed-resources/ejsubjectbrowse.html"))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML));
    }

    @Test
    public void testBioresearchSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=bioresearch-all&q=test").servletPath("/search.html"))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML));
    }

    @Test
    public void testCMEController() throws Exception {
        this.mockMvc.perform(get("/redirect/cme")).andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/redirect/cme?url=url")).andExpect(status().isFound())
                .andExpect(redirectedUrl("/secure/redirect/cme?url=url"));
        Map<String, Object> attributes = new HashMap<>();
        attributes.put(Model.USER, new User("ceyates@stanford.edu", "Charles E Yates", "ceyates@stanford.edu", "foo"));
        this.mockMvc.perform(get("/secure/redirect/cme?url=www.uptodate.com").sessionAttrs(attributes))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
        String url = "/redirect/cme?url=http://www.uptodate.com/foo?source=search_result&search=myocardial+infarction&selectedTitle=37%7E150";
        String redirect1 = "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/foo?source=search_result&unid=7629ef7dc159f69ed14476f452c194d0&srcsys=EZPX90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect1));
        attributes.put(Model.EMRID, "emrid");
        String redirect2 = "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/foo?source=search_result&unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect2));
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com").sessionAttrs(attributes))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
        String redirect3 = "http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com/").sessionAttrs(attributes)
                .header("X-FORWARDED-FOR", "171.65.1.202")).andExpect(status().isFound())
                .andExpect(redirectedUrl(redirect3));
        attributes.put(Model.EMRID, "lpch-emrid");
        String redirect4 = "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/contents/search?unid=lpch-emrid&srcsys=EPICLPCH90710&eiv=2.1.0";
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com/").sessionAttrs(attributes)
                .header("X-FORWARDED-FOR", "10.250.217.148")).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect4));
    }

    @Test
    public void testContentAwareRequestHandler() throws Exception {
        this.mockMvc.perform(get("/apple-touch-icon.png")).andExpect(status().isOk())
                .andExpect(content().contentType(IMAGE_PNG));
    }

    @Test
    public void testEresourceBrowse() throws Exception {
        this.mockMvc
                .perform(
                        get("/eresources/browse/type/Journal.html").servletPath("/eresources/browse/type/Journal.html"))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML));
    }

    @Test
    public void testIndex() throws Exception {
        this.mockMvc.perform(get("/index.html").servletPath("/index.html")).andExpect(status().isOk())
                .andExpect(content().contentType(TEXT_HTML));
    }

    @Test
    public void testIpGroupFetch() throws Exception {
        this.mockMvc.perform(get("/apps/ipGroupFetch?callback=foo")).andExpect(status().isOk())
                .andExpect(content().contentType(APPLICATION_JAVASCRIPT));
    }

    /**
     * Test basic Solr relevance. Only runs if edu.stanford.irt.laneweb.solr-url-laneSearch in a Solr instance is
     * accessible.
     *
     * @throws Exception
     */
    @Test
    public void testLaneSearch() throws Exception {
        if (solrLaneSearchIsReachable()) {
            Map<String, String> ns = new HashMap<>();
            ns.put("h", "http://www.w3.org/1999/xhtml");
            // science
            this.mockMvc.perform(get("/eresources/search.html?q=science").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='Science']", ns)
                            .exists())
                    .andExpect(content().contentType(TEXT_HTML));
            // nejm
            this.mockMvc.perform(get("/eresources/search.html?q=nejm").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='New England journal of medicine']",
                                    ns).exists());
            // PubMed
            this.mockMvc.perform(get("/eresources/search.html?q=PubMed").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='PubMed']", ns)
                            .exists());
            // pubmed
            this.mockMvc.perform(get("/eresources/search.html?q=pubmed").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='PubMed']", ns)
                            .exists());
            // known PMID
            this.mockMvc.perform(get("/eresources/search.html?q=20428285").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:div[@class='resultInfo']//h:a", ns)
                            .string("PMID: 20428285"))
                    .andExpect(xpath("//h:li", ns).nodeCount(1));
            // usmle OR nbme OR "examination questions"; limited to Lane Catalog and Book Digital
            this.mockMvc
                    .perform(
                            get("/eresources/search.html?source=all-all&q=usmle OR nbme OR \"examination questions\"&facets=recordType:\"bib\"::type:\"Book Digital\"")
                                    .servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:span[@class='primaryType']", ns).string("Book"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'USMLE')]", ns)
                                    .exists());
            // anatomy images
            this.mockMvc.perform(get("/eresources/search.html?q=anatomy images").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() <= 10]//h:a[@class='primaryLink' and @title='e-Anatomy']", ns)
                            .exists());
            // UpToDate and variants
            this.mockMvc.perform(get("/eresources/search.html?q=uptodate").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", ns)
                            .exists());
            this.mockMvc.perform(get("/eresources/search.html?q=UpToDate").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", ns)
                            .exists());
            this.mockMvc.perform(get("/eresources/search.html?q=Up To Date").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", ns)
                            .exists());
            this.mockMvc.perform(get("/eresources/search.html?q=Up-To-Date").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", ns)
                            .exists());
            // Harrison's
            this.mockMvc.perform(get("/eresources/search.html?q=Harrison's").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and starts-with(@title,\"Harrison's\")]",
                                    ns).exists());
            // Classes
            this.mockMvc.perform(get("/eresources/search.html?q=classes").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@href,'laneclasses.html')]",
                                    ns).exists());
            // EndNote
            this.mockMvc.perform(get("/eresources/search.html?q=EndNote").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li//h:a[@class='primaryLink' and contains(@title,'EndNote')]", ns)
                            .exists());
            // reference manager
            this.mockMvc
                    .perform(get("/eresources/search.html?q=reference manager").servletPath("/eresources/search.html"))
                    .andExpect(xpath("//h:li//h:a[@class='primaryLink' and contains(@title,'EndNote')]", ns)
                            .exists());
            // Bates Guide
            this.mockMvc.perform(get("/eresources/search.html?q=Bates Guide").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() <= 10]//h:a[@class='primaryLink' and @title=\"Bates' Guide to the physical examination videos\"]",
                                    ns).exists());
            // directions
            this.mockMvc
                    .perform(get("/eresources/search.html?q=directions to lane").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@href,'hours-directions.html')]",
                                    ns).exists());
            // Access Medicine
            this.mockMvc
                    .perform(get("/eresources/search.html?q=Access Medicine").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                    ns).exists());
            // AccessMedicine
            this.mockMvc.perform(get("/eresources/search.html?q=AccessMedicine").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                    ns).exists());
            // accessmedicine
            this.mockMvc.perform(get("/eresources/search.html?q=accessmedicine").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                    ns).exists());
            // journal of the american medical association (older JAMA title)
            this.mockMvc.perform(get("/eresources/search.html?q=journal of the american medical association")
                    .servletPath("/eresources/search.html")).andExpect(
                            xpath("//h:li[position() <= 10]//h:a[@class='primaryLink' and @title='Journal of the American Medical Association']",
                                    ns).exists());
            // jama
            this.mockMvc.perform(get("/eresources/search.html?q=jama").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='JAMA']", ns).exists());
            // jaad
            this.mockMvc.perform(get("/eresources/search.html?q=jaad").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='Journal of the American Academy of Dermatology']",
                                    ns).exists());
            // Guide to the evaluation of permanent impairment
            this.mockMvc.perform(get("/eresources/search.html?q=Guide to the evaluation of permanent impairment")
                    .servletPath("/eresources/search.html")).andExpect(
                            xpath("//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@title,'Guides to the evaluation of permanent impairment')]",
                                    ns).exists());
            // movement disorder
            this.mockMvc
                    .perform(get("/eresources/search.html?q=movement disorder").servletPath("/eresources/search.html"))
                    .andExpect(
                            xpath("//h:li[position() <= 10]//h:a[@class='primaryLink' and contains(@title,'Movement disorders')]",
                                    ns).exists());
        }
    }

    @Test
    public void testNotFoundServlet() throws Exception {
        this.mockMvc.perform(get("/rss/browse/type/video?a=z").servletPath("/rss/browse/type/video"))
                .andExpect(status().isNotFound()).andExpect(content().contentType(TEXT_HTML));
        this.mockMvc.perform(get("/rss/mesh/book?m=biology&page=all").servletPath("/rss/mesh/book"))
                .andExpect(status().isNotFound());
        this.mockMvc.perform(get("/wp-login.php").servletPath("/wp-login.php")).andExpect(status().isNotFound());
    }

    @Test
    public void testPubmedSearch() throws Exception {
        if (pubmedIsReachable()) {
            Map<String, String> ns = new HashMap<>();
            ns.put("h", "http://www.w3.org/1999/xhtml");
            // query term must appear within <strong> in first three results
            this.mockMvc
                    .perform(get("/apps/search/content/html/pubmed?q=skin")
                            .servletPath("/apps/search/content/html/pubmed"))
                    .andExpect(xpath("//h:li[position() <= 3]//h:a[@class='primaryLink']/h:strong", ns).exists())
                    .andExpect(content().contentType(TEXT_HTML));
        }
    }

    @Test
    public void testQueryMap() throws Exception {
        this.mockMvc
                .perform(get("/apps/querymap/json?q=ganz slipped capital femoral epiphysis").accept(APPLICATION_JSON))
                .andExpect(status().isOk()).andExpect(content().contentType(APPLICATION_JSON));
    }

    @Test
    public void testRedirects() throws Exception {
        this.mockMvc.perform(get("/beemap")).andExpect(status().isFound())
                .andExpect(header().string("location", "/beemap.html"));
        this.mockMvc.perform(get("/help/")).andExpect(status().isFound())
                .andExpect(header().string("location", "/help/index.html"));
        this.mockMvc.perform(get("/help")).andExpect(status().isFound())
                .andExpect(header().string("location", "/help/index.html"));
        this.mockMvc.perform(get("/help/me/")).andExpect(status().isFound())
                .andExpect(header().string("location", "/help/me/index.html"));
        this.mockMvc.perform(get("/help/me")).andExpect(status().isFound())
                .andExpect(header().string("location", "/help/me/index.html"));
    }

    @Test
    public void testSearchImage() throws Exception {
        this.mockMvc
                .perform(get("/apps/search/image?q=elephant&source=cc-images-all").servletPath("/apps/search/image"))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_XML));
    }

    @Test
    public void testTextbookSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=textbooks-all&q=test").servletPath("/search.html"))
                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML));
    }

    private boolean pubmedIsReachable() {
        boolean reachable = false;
        try {
            if (InetAddress.getByName("www.ncbi.nlm.nih.gov") != null) {
                reachable = true;
            }
        } catch (UnknownHostException e) {
            //
        }
        return reachable;
    }

    private boolean solrLaneSearchIsReachable() throws Exception {
        boolean reachable = false;
        MockHttpServletResponse response = this.mockMvc.perform(get("/status.txt").servletPath("/status.txt"))
                .andReturn().getResponse();
        if (200 == response.getStatus()) {
            reachable = !response.getContentAsString().contains("[ERROR] suggestions failed");
        }
        return reachable;
    }
}
