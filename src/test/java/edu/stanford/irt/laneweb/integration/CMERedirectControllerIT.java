package edu.stanford.irt.laneweb.integration;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrl;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.webAppContextSetup;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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
public class CMERedirectControllerIT {

    private static final User USER = new User("ceyates@stanford.edu", "Charles E Yates", "ceyates@stanford.edu", "foo");

    private MockMvc mockMvc;

    @Resource
    private WebApplicationContext webApplicationContext;

    @Before
    public void setupFixture() {
        this.mockMvc = webAppContextSetup(this.webApplicationContext).build();
    }

    @Test
    public void testCMEControllerBadRequest() throws Exception {
        this.mockMvc.perform(get("/redirect/cme")).andExpect(status().isBadRequest());
    }

    @Test
    public void testCMEControllerGoodRequest() throws Exception {
        this.mockMvc.perform(get("/redirect/cme?url=url")).andExpect(status().isFound())
                .andExpect(redirectedUrl("/secure/redirect/cme?url=url"));
    }

    @Test
    public void testCMEControllerLPCH() throws Exception {
        Map<String, Object> attributes = new HashMap<>(Collections.singletonMap(Model.USER, USER));
        attributes.put(Model.EMRID, "lpch-emrid");
        String redirect4 = "https://login.laneproxy.stanford.edu/login?url=https://www.uptodate.com/contents/search?unid=lpch-emrid&srcsys=EPICLPCH90710&eiv=2.1.0";
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com/").sessionAttrs(attributes)
                .header("X-FORWARDED-FOR", "45.42.34.136")).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect4));
    }

    @Test
    public void testCMEControllerRedirect1() throws Exception {
        Map<String, Object> attributes = Collections.singletonMap(Model.USER, USER);
        String url = "/redirect/cme?url=https://www.uptodate.com/foo?source=search_result&search=myocardial+infarction&selectedTitle=37%7E150";
        String redirect1 = "https://login.laneproxy.stanford.edu/login?url=https://www.uptodate.com/foo?source=search_result&unid=7629ef7dc159f69ed14476f452c194d0&srcsys=EZPX90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect1));
    }

    @Test
    public void testCMEControllerRedirect2() throws Exception {
        String url = "/redirect/cme?url=https://www.uptodate.com/foo?source=search_result&search=myocardial+infarction&selectedTitle=37%7E150";
        Map<String, Object> attributes = Collections.singletonMap(Model.EMRID, "emrid");
        String redirect2 = "https://login.laneproxy.stanford.edu/login?url=https://www.uptodate.com/foo?source=search_result&unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc.perform(get(url).sessionAttrs(attributes)).andExpect(status().isFound())
                .andExpect(redirectedUrlPattern(redirect2));
    }

    @Test
    public void testCMEControllerSHC() throws Exception {
        Map<String, Object> attributes = new HashMap<>(Collections.singletonMap(Model.USER, USER));
        attributes.put(Model.EMRID, "emrid");
        String redirect3 = "https://www.uptodate.com/contents/search?unid=emrid&srcsys=epic90710&eiv=2.1.0";
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com/").sessionAttrs(attributes)
                .header("X-FORWARDED-FOR", "171.65.1.202")).andExpect(status().isFound())
                .andExpect(redirectedUrl(redirect3));
    }

    @Test
    public void testCMEControllerUpToDate() throws Exception {
        Map<String, Object> attributes = Collections.singletonMap(Model.USER, USER);
        this.mockMvc.perform(get("/redirect/cme?url=www.uptodate.com").sessionAttrs(attributes))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
    }

    @Test
    public void testCMEControllerUser() throws Exception {
        Map<String, Object> attributes = Collections.singletonMap(Model.USER, USER);
        this.mockMvc.perform(get("/secure/redirect/cme?url=www.uptodate.com").sessionAttrs(attributes))
                .andExpect(status().isFound())
                .andExpect(redirectedUrl("https://login.laneproxy.stanford.edu/login?url=www.uptodate.com"));
    }
}
