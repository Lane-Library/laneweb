package edu.stanford.irt.laneweb.config;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.cocoon.sitemap.expression.Variable;
import edu.stanford.irt.cocoon.sitemap.match.Matcher;
import edu.stanford.irt.cocoon.sitemap.match.ParameterRegexpMatcher;
import edu.stanford.irt.cocoon.sitemap.match.RegexpMatcher;
import edu.stanford.irt.cocoon.sitemap.match.URLDecodingMatcher;
import edu.stanford.irt.cocoon.sitemap.match.WildcardMatcher;
import edu.stanford.irt.cocoon.sitemap.select.ModelAttributeSelector;
import edu.stanford.irt.cocoon.sitemap.select.Selector;
import edu.stanford.irt.cocoon.spring.VariablePropertyEditor;
import edu.stanford.irt.laneweb.cocoon.CacheableSelector;
import edu.stanford.irt.laneweb.ipgroup.IPGroupSelector;

@Configuration
public class SitemapConfiguration {

    private static final String SITEMAP_URI = "sitemap-uri";

    @Bean
    public static CustomEditorConfigurer customEditorConfigurer() {
        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();
        customEditors.put(Variable.class, VariablePropertyEditor.class);
        configurer.setCustomEditors(customEditors);
        return configurer;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/cacheable")
    public Selector cacheableSelector() {
        return new CacheableSelector();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/facets")
    public Selector facetsSelector() {
        ModelAttributeSelector selector = new ModelAttributeSelector();
        selector.setAttributeName("facets");
        return selector;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/ipgroup")
    public Selector ipGroupSelector() {
        return new IPGroupSelector();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/logged-in")
    public Selector loggedInSelector() {
        ModelAttributeSelector selector = new ModelAttributeSelector();
        selector.setAttributeName("userid");
        return selector;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/regex-parameter")
    public Matcher parameterRegexpMatcher() {
        return new ParameterRegexpMatcher();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/regexp")
    public Matcher regexpMatcher() {
        return new RegexpMatcher(SITEMAP_URI);
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/url-decoding-wildcard")
    public Matcher urlDecodingMatcher() {
        return new URLDecodingMatcher(SITEMAP_URI);
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/wildcard")
    public Matcher wildcardMatcher() {
        return new WildcardMatcher(SITEMAP_URI);
    }
}
