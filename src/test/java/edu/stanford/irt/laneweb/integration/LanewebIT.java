package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.xpath;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

import edu.stanford.irt.laneweb.Laneweb;
import jakarta.annotation.Resource;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@SpringBootTest(classes = Laneweb.class)
public class LanewebIT {

        private static final MediaType APPLICATION_JAVASCRIPT = new MediaType("application", "javascript",
                        StandardCharsets.UTF_8);

        private static final MediaType IMAGE_PNG = new MediaType("image", "png");

        private static final MediaType TEXT_HTML = new MediaType("text", "html", StandardCharsets.UTF_8);

        private MockMvc mockMvc;

        @Resource
        private WebApplicationContext webApplicationContext;

        @Before
        public void setupFixture() {
                this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
        }

        @Test
        public void testClinicalSearch() throws Exception {
                Map<String, String> ns = new HashMap<>();
                ns.put("h", "http://www.w3.org/1999/xhtml");
                this.mockMvc.perform(get("/search.html?source=clinical-all&q=test").servletPath("/search.html"))
                                .andExpect(status().isOk())
                                .andExpect(
                                                xpath("//h:li[position() >= 10]//h:a[@class='primaryLink bookmarking']/h:strong",
                                                                ns).exists())
                                .andExpect(content().contentType(TEXT_HTML));
        }

        @Test
        public void testContentAwareRequestHandler() throws Exception {
                this.mockMvc.perform(get("/apple-touch-icon.png")).andExpect(status().isOk())
                                .andExpect(content().contentType(IMAGE_PNG));
        }

        @Test
        public void testEresourceBrowse() throws Exception {
                this.mockMvc
                                .perform(get("/eresources/browse/query/type:Journal.html")
                                                .servletPath("/eresources/browse/query/type:Journal.html"))
                                .andExpect(status().isOk()).andExpect(content().contentType(TEXT_HTML));
        }

        @Test
        public void testGetSuggestionList() throws Exception {
                this.mockMvc.perform(get("/apps/suggest/getSuggestionList?q=cardio")).andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
        }

        @Test
        public void testGetSuggestions() throws Exception {
                this.mockMvc.perform(get("/apps/suggest/json?q=cardio")).andExpect(status().isOk())
                                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
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

        @Test
        public void testNotFoundServlet() throws Exception {
                this.mockMvc.perform(get("/rss/browse/type/video?a=z").servletPath("/rss/browse/type/video"))
                                .andExpect(status().isNotFound()).andExpect(content().contentType(TEXT_HTML));
                this.mockMvc.perform(get("/rss/mesh/book?m=biology&page=all").servletPath("/rss/mesh/book"))
                                .andExpect(status().isNotFound());
                this.mockMvc.perform(get("/wp-login.php").servletPath("/wp-login.php"))
                                .andExpect(status().isNotFound());
        }

        @Test
        public void testPubmedSearch() throws Exception {
                Map<String, String> ns = new HashMap<>();
                ns.put("h", "http://www.w3.org/1999/xhtml");
                // query term must appear within <strong> in first three results
                this.mockMvc
                                .perform(
                                                get("/apps/search/content/html/pubmed?q=Ebola")
                                                                .servletPath("/apps/search/content/html/pubmed"))
                                .andExpect(
                                                xpath("//h:li[position() <= 3]//h:a[@class='primaryLink bookmarking']/h:strong",
                                                                ns).exists())
                                .andExpect(content().contentType(TEXT_HTML));
        }

        @Test
        public void testRedirects() throws Exception {
                this.mockMvc.perform(get("/beemap")).andExpect(status().isFound())
                                .andExpect(header().string("location", "/beemap.html"));
                this.mockMvc.perform(get("/help/")).andExpect(status().isFound())
                                .andExpect(header().string("location", "/help/index.html"));
                this.mockMvc.perform(get("/help/me/")).andExpect(status().isFound())
                                .andExpect(header().string("location", "/help/me/index.html"));
        }
}
