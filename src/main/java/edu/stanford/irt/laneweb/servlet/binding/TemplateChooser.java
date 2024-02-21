package edu.stanford.irt.laneweb.servlet.binding;

import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Pattern;

import jakarta.servlet.http.HttpServletRequest;

import edu.stanford.irt.laneweb.model.Model;

/**
 * This class contains the logic to determine which template is used. The highest priority is given to the template
 * request parameter. Next priority is a Map with regular expressions matching request URIs as the key with the
 * associated template as the value. Finally, default value is provided.
 */
public class TemplateChooser {

    /** the name of the default template */
    private String defaultTemplate;

    private Set<String> existingTemplates;

    /** a Map used internally that contains the compiled regular expressions */
    private Map<String, Pattern> patternMap;

    /** a Map of regular expressions associated with templates */
    private Map<String, String> templateMap = Collections.emptyMap();

    public TemplateChooser(final String defaultTemplate, final Set<String> existingTemplates,
            final Map<String, String> templateMap) {
        if (null == defaultTemplate) {
            throw new IllegalArgumentException("null defaultTemplate");
        }
        if (null != templateMap) {
            this.templateMap = templateMap;
            this.patternMap = new LinkedHashMap<>(templateMap.size());
            for (String pattern : templateMap.keySet()) {
                this.patternMap.put(pattern, Pattern.compile(pattern));
            }
        }
        this.defaultTemplate = defaultTemplate;
        this.existingTemplates = new HashSet<>(existingTemplates);
    }

    public String getDefaultTemplate() {
        return this.defaultTemplate;
    }

    public String getTemplate(final HttpServletRequest request) {
        String template = request.getParameter(Model.TEMPLATE);
        if (!this.existingTemplates.contains(template)) {
            template = null;
        }
        if (null == template && !this.templateMap.isEmpty()) {
            String uri = request.getServletPath();
            for (Entry<String, Pattern> entry : this.patternMap.entrySet()) {
                if (entry.getValue().matcher(uri).matches()) {
                    template = this.templateMap.get(entry.getKey());
                    break;
                }
            }
        }
        return null == template ? this.defaultTemplate : template;
    }
}
