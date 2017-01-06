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

    @Bean
    public RedirectProcessor redirectProcessor() {
        List<RedirectProcessor> redirectProcessors = new ArrayList<>(3);
        redirectProcessors.add(new TrailingSlashRedirectProcessor());
        Map<String, String> redirectMap = new HashMap<>();
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
        Map<String, String> shcRedirectMap = new HashMap<>();
        shcRedirectMap.put("/shc/anesthesia.html(?:\\??)(.*)",
                "/portals/anesthesia.html?sourceid=shc&source=/portals/anesthesia.html&$1");
        shcRedirectMap.put("/shc/cardiology.html(?:\\??)(.*)",
                "/portals/cardiology.html?sourceid=shc&source=/portals/cardiology.html&$1");
        shcRedirectMap.put("/shc/clinical.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/ear-nose-throat.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/emergency.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/gastroenterology.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/hematology.html(?:\\??)(.*)", "/portals/hematology.html?sourceid=shc&$1");
        shcRedirectMap.put("/shc/hepatology.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/icu.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/infectious-disease.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/internal-medicine.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/neurology.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/ob-gyn.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/oncology.html(?:\\??)(.*)",
                "/portals/internal-medicine.html?sourceid=shc&source=/portals/internal-medicine.html&$1");
        shcRedirectMap.put("/shc/ophthalmology.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/orthopedics.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/psychiatry.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/pulmonary.html(?:\\??)(.*)",
                "/portals/pulmonary.html?sourceid=shc&source=/portals/pulmonary.html&$1");
        shcRedirectMap.put("/shc/radiology.html(?:\\?q=?)(.*)",
                "http://www.guideline.gov/search/results.aspx?113=666&term=$1");
        shcRedirectMap.put("/shc/surgery-cardiothoracic.html(?:\\??)(.*)",
                "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/surgery-general.html(?:\\??)(.*)", "/search.html?sourceid=shc&source=clinical-all&$1");
        shcRedirectMap.put("/shc/portals/shc.html(?:\\??)(.*)", "/portals/shc.html?sourceid=shc&$1");
        shcRedirectProcessor.setRedirectMap(shcRedirectMap);
        redirectProcessors.add(shcRedirectProcessor);
        return new CompositeRedirectProcessor(redirectProcessors);
    }
}
