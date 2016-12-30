package edu.stanford.irt.laneweb.config;

import java.beans.PropertyEditor;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.config.CustomEditorConfigurer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import edu.stanford.irt.cocoon.sitemap.expression.Variable;
import edu.stanford.irt.cocoon.sitemap.match.ParameterRegexpMatcher;
import edu.stanford.irt.cocoon.sitemap.match.RegexpMatcher;
import edu.stanford.irt.cocoon.sitemap.match.URLDecodingMatcher;
import edu.stanford.irt.cocoon.sitemap.match.WildcardMatcher;
import edu.stanford.irt.cocoon.sitemap.select.ModelAttributeSelector;
import edu.stanford.irt.cocoon.spring.VariablePropertyEditor;
import edu.stanford.irt.laneweb.cocoon.CacheableSelector;
import edu.stanford.irt.laneweb.ipgroup.IPGroupSelector;

@Configuration
public class SitemapConfiguration {

    @Bean
    public static CustomEditorConfigurer customEditorConfigurer() {
        CustomEditorConfigurer configurer = new CustomEditorConfigurer();
        Map<Class<?>, Class<? extends PropertyEditor>> customEditors = new HashMap<>();
        customEditors.put(Variable.class, VariablePropertyEditor.class);
        configurer.setCustomEditors(customEditors);
        return configurer;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/cacheable")
    public CacheableSelector cacheableSelector() {
        return new CacheableSelector();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/facets")
    public ModelAttributeSelector facetsSelector() {
        ModelAttributeSelector selector = new ModelAttributeSelector();
        selector.setAttributeName("facets");
        return selector;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/ipgroup")
    public IPGroupSelector ipGroupSelector() {
        return new IPGroupSelector();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.select.Selector/logged-in")
    public ModelAttributeSelector loggedInSelector() {
        ModelAttributeSelector selector = new ModelAttributeSelector();
        selector.setAttributeName("userid");
        return selector;
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/regex-parameter")
    public ParameterRegexpMatcher parameterRegexpMatcher() {
        return new ParameterRegexpMatcher();
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/regexp")
    public RegexpMatcher regexpMatcher() {
        return new RegexpMatcher("sitemap-uri");
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/url-decoding-wildcard")
    public URLDecodingMatcher urlDecodingMatcher() {
        return new URLDecodingMatcher("sitemap-uri");
    }

    @Bean(name = "edu.stanford.irt.cocoon.sitemap.match.Matcher/wildcard")
    public WildcardMatcher wildcardMatcher() {
        return new WildcardMatcher("sitemap-uri");
    }
}
