package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.ContextHierarchy;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.model.Model;
import edu.stanford.irt.laneweb.servlet.mvc.WebMvcConfigurer;
import edu.stanford.irt.laneweb.user.User;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextHierarchy({
    @ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/spring/laneweb.xml", initializers = LanewebContextInitializer.class),
    @ContextConfiguration(classes = WebMvcConfigurer.class, initializers = LanewebContextInitializer.class)})
public class LanewebIT {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupFixture() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testBioresearchSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=bioresearch-all&q=test").servletPath("/search.html")).andExpect(
                status().isOk());
    }

    @Test
    public void testCMEController() throws Exception {
        this.mockMvc.perform(get("/redirect/cme")).andExpect(status().isBadRequest());
        this.mockMvc.perform(get("/redirect/cme?url=url")).andExpect(status().isFound())
                .andExpect(redirectedUrl("/secure/redirect/cme?url=url"));
        this.mockMvc.perform(get("/secure/redirect/cme?url=www.uptodate.com")).andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
        Map<String, Object> attributes = new HashMap<String, Object>();
        attributes.put(Model.USER, new User("ceyates@stanford.edu", "Charles E Yates", "ceyates@stanford.edu", "foo"));
        String url = "/redirect/cme?url=http://www.uptodate.com/foo?source=search_result&search=myocardial+infarction&selectedTitle=37%7E150";
        String redirect1 = "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/foo?source=search_result&unid=7629ef7dc159f69ed14476f452c194d0&srcsys=EZPX90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrl(redirect1));
        attributes.put(Model.EMRID, "emrid");
        String redirect2 = "https://login.laneproxy.stanford.edu/login?url=http://www.uptodate.com/foo?source=search_result&unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrl(redirect2));
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com").sessionAttrs(attributes))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
        String redirect3 = "http://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc
                .perform(
                        get("/redirect/cme?url=www.uptodate.com/").sessionAttrs(attributes).header("X-FORWARDED-FOR",
                                "171.65.1.202")).andExpect(status().isFound()).andExpect(redirectedUrl(redirect3));
    }

    @Test
    public void testContentAwareRequestHandler() throws Exception {
        this.mockMvc.perform(get("/apple-touch-icon.png")).andExpect(status().isOk());
    }

    @Test
    public void testIndex() throws Exception {
        this.mockMvc.perform(get("/index.html").servletPath("/index.html")).andExpect(status().isOk());
    }

    @Test
    public void testPubmedSearch() throws Exception {
        if (pubmedIsReachable()) {
            Map<String, String> ns = new HashMap<String, String>();
            ns.put("h", "http://www.w3.org/1999/xhtml");
            // query term must appear within <strong> in first three results
            this.mockMvc.perform(
                    get("/apps/search/content/html/pubmed?q=test").servletPath("/apps/search/content/html/pubmed"))
                    .andExpect(xpath("//h:li[position() <= 3]//h:a[@class='primaryLink']/h:strong", ns).exists());
        }
    }

    @Test
    public void testTextbookSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=textbooks-all&q=test").servletPath("/search.html")).andExpect(
                status().isOk());
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
}
