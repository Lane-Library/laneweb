package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
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
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.context.WebApplicationContext;

@RunWith(SpringJUnit4ClassRunner.class)
@WebAppConfiguration
@ActiveProfiles("test")
@ContextConfiguration(locations = { "classpath:net/bull/javamelody/monitoring-spring.xml",
        "file:src/main/webapp/WEB-INF/spring/bassett.xml", "file:src/main/webapp/WEB-INF/spring/binding.xml",
        "file:src/main/webapp/WEB-INF/spring/bookmarks.xml", "file:src/main/webapp/WEB-INF/spring/cme.xml",
        "file:src/main/webapp/WEB-INF/spring/datasources.xml", "file:src/main/webapp/WEB-INF/spring/eresources.xml",
        "file:src/main/webapp/WEB-INF/spring/logger.xml",
        "file:src/main/webapp/WEB-INF/spring/laneweb.xml", "file:src/main/webapp/WEB-INF/spring/mapping.xml",
        "file:src/main/webapp/WEB-INF/spring/pipeline.xml", "file:src/main/webapp/WEB-INF/spring/proxy.xml",
        "file:src/main/webapp/WEB-INF/spring/querymap.xml", "file:src/main/webapp/WEB-INF/spring/redirect.xml",
        "file:src/main/webapp/WEB-INF/spring/search.xml", "file:src/main/webapp/WEB-INF/spring/sitemap.xml",
        "file:src/main/webapp/WEB-INF/spring/source.xml", "file:src/main/webapp/WEB-INF/spring/suggest.xml",
        "file:src/main/webapp/WEB-INF/spring/trends.xml", "file:src/main/webapp/WEB-INF/spring/voyager-login.xml",
        "file:src/main/webapp/applications.xmap", "file:src/main/webapp/bookmarks.xmap",
        "file:src/main/webapp/classes.xmap", "file:src/main/webapp/content.xmap",
        "file:src/main/webapp/eresources.xmap", "file:src/main/webapp/mobile.xmap", "file:src/main/webapp/rss.xmap",
        "file:src/main/webapp/sitemap.xmap", "file:src/main/webapp/WEB-INF/spring/laneweb-servlet.xml" }, initializers = LanewebContextInitializer.class)
public class LanewebIT {

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    private boolean pubmedIsReachable () {
        boolean reachable = false;
        try {
            if (InetAddress.getByName("www.ncbi.nlm.nih.gov") != null ) {
                reachable = true;
            }
        } catch (UnknownHostException e) {
            //
        }
        return reachable;
    }
    
    @Before
    public void setupFixture() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testIndex() throws Exception {
        this.mockMvc.perform(get("/index.html")).andExpect(status().isOk());
    }
    
    @Test
    public void testBioresearchSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=bioresearch-all&q=test")).andExpect(status().isOk());
    }

    @Test
    public void testPubmedSearch() throws Exception {
        if (pubmedIsReachable()) {
            Map<String, String> ns = new HashMap<String, String>();
            ns.put("h", "http://www.w3.org/1999/xhtml");
            // query term must appear within <strong> in first three results
            this.mockMvc.perform(get("/apps/search/content/html/pubmed?q=test")).andExpect(xpath("//h:li[position() <= 3]//h:a[@class='primaryLink']/h:strong", ns, new Object()).exists());
        }
    }
    
    @Test
    public void testTextbookSearch() throws Exception {
        this.mockMvc.perform(get("/search.html?source=textbooks-all&q=test")).andExpect(status().isOk());
    }
    
    @Test
    public void testContentAwareRequestHandler() throws Exception {
        this.mockMvc.perform(get("/apple-touch-icon.png")).andExpect(status().isOk());
    }
}
