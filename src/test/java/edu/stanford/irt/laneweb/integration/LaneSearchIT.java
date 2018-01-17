package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ContextConfiguration(classes = edu.stanford.irt.laneweb.config.LanewebConfiguration.class)
public class LaneSearchIT {

    private static final MediaType TEXT_HTML = new MediaType("text", "html", StandardCharsets.UTF_8);

    private MockMvc mockMvc;

    private Map<String, String> ns;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupFixture() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        this.ns = Collections.singletonMap("h", "http://www.w3.org/1999/xhtml");
    }

    @Test
    public void testLaneSearch20428285() throws Exception {
        // known PMID
        this.mockMvc.perform(get("/eresources/search.html?q=20428285").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:div[@class='resultInfo']//h:a", this.ns)
                        .string("PMID: 20428285"))
                .andExpect(xpath("//h:li", this.ns).nodeCount(1));
    }

    @Test
    public void testLaneSearchAccess_Medicine() throws Exception {
        // Access Medicine
        this.mockMvc.perform(get("/eresources/search.html?q=Access Medicine").servletPath("/eresources/search.html"))
                .andExpect(
                        xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                this.ns).exists());
    }

    @Test
    public void testLaneSearchaccessmedicine() throws Exception {
        // accessmedicine
        this.mockMvc.perform(get("/eresources/search.html?q=accessmedicine").servletPath("/eresources/search.html"))
                .andExpect(
                        xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                this.ns).exists());
    }

    @Test
    public void testLaneSearchAccessMedicine() throws Exception {
        // AccessMedicine
        this.mockMvc.perform(get("/eresources/search.html?q=AccessMedicine").servletPath("/eresources/search.html"))
                .andExpect(
                        xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'AccessMedicine')]",
                                this.ns).exists());
    }

    @Test
    public void testLaneSearchAnatomyImages() throws Exception {
        // anatomy images
        this.mockMvc.perform(get("/eresources/search.html?q=anatomy images").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() <= 10]//h:a[@class='primaryLink' and @title='e-Anatomy']", this.ns)
                        .exists());
    }

    @Test
    public void testLaneSearchBatesGuide() throws Exception {
        // Bates Guide
        this.mockMvc.perform(get("/eresources/search.html?q=Bates Guide").servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() <= 10]//h:a[@class='primaryLink' and @title=\"Bates' Guide to the physical examination videos\"]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchClasses() throws Exception {
        // Classes
        this.mockMvc.perform(get("/eresources/search.html?q=classes").servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@href,'laneclasses.html')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchDirections() throws Exception {
        // directions
        this.mockMvc.perform(get("/eresources/search.html?q=directions to lane").servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@href,'directions-maps.html')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchEndNote() throws Exception {
        // EndNote
        this.mockMvc.perform(get("/eresources/search.html?q=EndNote").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li//h:a[@class='primaryLink' and contains(@title,'EndNote')]", this.ns).exists());
    }

    @Test
    public void testLaneSearchEndPages() throws Exception {
        // citation search variant 3: end pages
        this.mockMvc
                .perform(get("/eresources/search.html?q=JMLA. 2010 4 98(2):171-175.")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@href,'20428285')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchGuide() throws Exception {
        // Guide to the evaluation of permanent impairment
        this.mockMvc
                .perform(get("/eresources/search.html?q=Guide to the evaluation of permanent impairment")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@title,'Guides to the evaluation of permanent impairment')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchHarrisons() throws Exception {
        // Harrison's
        this.mockMvc.perform(get("/eresources/search.html?q=Harrison's").servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() = 1]//h:a[@class='primaryLink' and starts-with(@title,\"Harrison's\")]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchJAAD() throws Exception {
        // jaad
        this.mockMvc.perform(get("/eresources/search.html?q=jaad").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='Journal of the American Academy of Dermatology']",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchJAMA() throws Exception {
        // jama
        this.mockMvc.perform(get("/eresources/search.html?q=jama").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='JAMA']", this.ns).exists());
    }

    @Test
    public void testLaneSearchLongFormMonth() throws Exception {
        // citation search variant 1: long form month
        this.mockMvc
                .perform(get("/eresources/search.html?q=J Med Libr Assoc. 2010 April 98(2):171-5.")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@href,'20428285')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchMovementDisorder() throws Exception {
        // movement disorder
        this.mockMvc.perform(get("/eresources/search.html?q=movement disorder").servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() <= 10]//h:a[@class='primaryLink' and contains(@title,'Movement disorders')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchNEJM() throws Exception {
        // nejm
        this.mockMvc.perform(get("/eresources/search.html?q=nejm").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='New England journal of medicine']",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchNumericMonth() throws Exception {
        // citation search variant 2: numeric month
        this.mockMvc
                .perform(get("/eresources/search.html?q=JMLA. 2010 4 98(2):171-5.")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() <= 5]//h:a[@class='primaryLink' and contains(@href,'20428285')]",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchOldJAMATitle() throws Exception {
        // journal of the american medical association (older JAMA title)
        this.mockMvc
                .perform(get("/eresources/search.html?q=journal of the american medical association")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath(
                        "//h:li[position() <= 10]//h:a[@class='primaryLink' and @title='Journal of the American Medical Association']",
                        this.ns).exists());
    }

    @Test
    public void testLaneSearchpubmed() throws Exception {
        this.mockMvc.perform(get("/eresources/search.html?q=pubmed").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='PubMed']", this.ns).exists());
    }

    @Test
    public void testLaneSearchPubMed() throws Exception {
        // PubMed
        this.mockMvc.perform(get("/eresources/search.html?q=PubMed").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='PubMed']", this.ns).exists());
        // pubmed
    }

    @Test
    public void testLaneSearchReferenceManager() throws Exception {
        // reference manager
        this.mockMvc.perform(get("/eresources/search.html?q=reference manager").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li//h:a[@class='primaryLink' and contains(@title,'EndNote')]", this.ns).exists());
    }

    /**
     * Test basic Solr relevance. Only runs if edu.stanford.irt.laneweb.solr-url-laneSearch in a Solr instance is
     * accessible.
     *
     * @throws Exception
     */
    @Test
    public void testLaneSearchScience() throws Exception {
        // science
        this.mockMvc.perform(get("/eresources/search.html?q=science").servletPath("/eresources/search.html")).andExpect(
                xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='Science']", this.ns).exists())
                .andExpect(content().contentType(TEXT_HTML));
    }

    @Test
    public void testLaneSearchUp_To_Date() throws Exception {
        this.mockMvc.perform(get("/eresources/search.html?q=Up To Date").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", this.ns)
                        .exists());
    }

    @Test
    public void testLaneSearchUpdashTodashDate() throws Exception {
        this.mockMvc.perform(get("/eresources/search.html?q=Up-To-Date").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", this.ns)
                        .exists());
    }

    @Test
    public void testLaneSearchuptodate() throws Exception {
        // UpToDate and variants
        this.mockMvc.perform(get("/eresources/search.html?q=uptodate").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", this.ns)
                        .exists());
    }

    @Test
    public void testLaneSearchUpToDate() throws Exception {
        this.mockMvc.perform(get("/eresources/search.html?q=UpToDate").servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and @title='UpToDate']", this.ns)
                        .exists());
    }

    @Test
    public void testLaneSearchUSMLEEtc() throws Exception {
        // usmle OR nbme OR "examination questions"; limited to Lane Catalog and Book Digital
        this.mockMvc.perform(get(
                "/eresources/search.html?source=all-all&q=usmle OR nbme OR \"examination questions\"&facets=recordType:\"bib\"::type:\"Book Digital\"")
                        .servletPath("/eresources/search.html"))
                .andExpect(xpath("//h:li[position() = 1]//h:span[@class='primaryType']", this.ns).string("Book"))
                .andExpect(
                        xpath("//h:li[position() = 1]//h:a[@class='primaryLink' and contains(@title,'USMLE')]", this.ns)
                                .exists());
    }
}
