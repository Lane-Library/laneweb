package edu.stanford.irt.laneweb.config;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.laneweb.servlet.redirect.CompositeRedirectProcessor;
import edu.stanford.irt.laneweb.servlet.redirect.DefaultRedirectProcessor;
import edu.stanford.irt.laneweb.servlet.redirect.RedirectProcessor;
import edu.stanford.irt.laneweb.servlet.redirect.SHCRedirectProcessor;
import edu.stanford.irt.laneweb.servlet.redirect.TrailingSlashRedirectProcessor;

@Configuration
public class RedirectConfiguration {

    private static final String SEARCH_CLINICAL = "/search.html?sourceid=shc&source=clinical-all&$1";

    @Bean
    public RedirectProcessor redirectProcessor() {
        List<RedirectProcessor> redirectProcessors = new ArrayList<>(3);
        redirectProcessors.add(new TrailingSlashRedirectProcessor());
        Map<String, String> redirectMap = new HashMap<>(13);
        redirectMap.put("/classes/index\\.html", "/classes-consult/laneclasses.html");
        redirectMap.put("/lksc-print(?:.*)", "/help/lksc-print.html");
        redirectMap.put("/classes", "/classes-consult/laneclasses.html");
        redirectMap.put("/m", "/m/index.html");
        redirectMap.put("/beemap", "/beemap.html");
        redirectMap.put("/about/contact.html", "/contacts/index.html");
        redirectMap.put("/about/staff-dir.html", "/contacts/staff-dir.html");
        redirectMap.put("/about/liaisons.html", "/contacts/liaisons.html");
        redirectMap.put("/about/libadmin.html", "/contacts/mgm-team.html");
        redirectMap.put("/portals/lpch-cerner.html\\?(sourceid=cerner&q=.*)", "/search.html?source=peds-all&$1");
        redirectMap.put("/using-lib/computing.html", "/using-lib/study-computing-spaces.html");
        DefaultRedirectProcessor redirectProcessor = new DefaultRedirectProcessor(redirectMap);
        redirectProcessors.add(redirectProcessor);
        Map<String, String> shcRedirectMap = new HashMap<>(6);
        shcRedirectMap.put("/shc/cardiology.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/gastroenterology.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/icu.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/infectious-disease.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/internal-medicine.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/oncology.html(?:\\??)(.*)", SEARCH_CLINICAL);
        SHCRedirectProcessor shcRedirectProcessor = new SHCRedirectProcessor(shcRedirectMap);
        redirectProcessors.add(shcRedirectProcessor);
        return new CompositeRedirectProcessor(redirectProcessors);
    }
}
