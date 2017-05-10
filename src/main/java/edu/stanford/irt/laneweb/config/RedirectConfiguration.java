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

    private static final String SEARCH_INTERNAL_MEDICINE = "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1";

    @Bean
    public RedirectProcessor redirectProcessor() {
        List<RedirectProcessor> redirectProcessors = new ArrayList<>(3);
        redirectProcessors.add(new TrailingSlashRedirectProcessor());
        Map<String, String> redirectMap = new HashMap<>(6);
        redirectMap.put("/classes/index\\.html", "/classes-consult/laneclasses.html");
        redirectMap.put("/lksc-print(?:.*)", "/help/lksc-print.html");
        redirectMap.put("/classes", "/classes-consult/laneclasses.html");
        redirectMap.put("/m", "/m/index.html");
        redirectMap.put("/imagesearch.html", "/bioimagesearch.html");
        redirectMap.put("/beemap", "/beemap.html");
        DefaultRedirectProcessor redirectProcessor = new DefaultRedirectProcessor();
        redirectProcessor.setRedirectMap(redirectMap);
        redirectProcessors.add(redirectProcessor);
        SHCRedirectProcessor shcRedirectProcessor = new SHCRedirectProcessor();
        Map<String, String> shcRedirectMap = new HashMap<>(23);
        shcRedirectMap.put("/shc/anesthesia.html(?:\\??)(.*)",
                "/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&$1");
        shcRedirectMap.put("/shc/cardiology.html(?:\\??)(.*)",
                "/portals/cardiology.html?sourceid=shc&source=/portals/cardiology.html&$1");
        shcRedirectMap.put("/shc/clinical.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/ear-nose-throat.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/emergency.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/gastroenterology.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/hematology.html(?:\\??)(.*)", "/portals/hematology.html?sourceid=shc&$1");
        shcRedirectMap.put("/shc/hepatology.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/icu.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/infectious-disease.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/internal-medicine.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/neurology.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/ob-gyn.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/oncology.html(?:\\??)(.*)", SEARCH_INTERNAL_MEDICINE);
        shcRedirectMap.put("/shc/ophthalmology.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/orthopedics.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/psychiatry.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/pulmonary.html(?:\\??)(.*)",
                "/portals/pulmonary.html?sourceid=shc&source=/portals/pulmonary.html&$1");
        shcRedirectMap.put("/shc/radiology.html(?:\\?q=?)(.*)",
                "http://www.guideline.gov/search/results.aspx?113=666&term=$1");
        shcRedirectMap.put("/shc/surgery-cardiothoracic.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/surgery-general.html(?:\\??)(.*)", SEARCH_CLINICAL);
        shcRedirectMap.put("/shc/portals/shc.html(?:\\??)(.*)", "/portals/shc.html?sourceid=shc&$1");
        shcRedirectProcessor.setRedirectMap(shcRedirectMap);
        redirectProcessors.add(shcRedirectProcessor);
        return new CompositeRedirectProcessor(redirectProcessors);
    }
}
