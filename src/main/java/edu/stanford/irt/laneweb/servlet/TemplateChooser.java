package edu.stanford.irt.laneweb.servlet;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Pattern;

import javax.servlet.http.HttpServletRequest;

/**
 * This class contains the logic to determine which template is used. The highest priority is given to the template
 * request parameter. Next priority is a Map with regular expressions matching request URIs as the key with the
 * associated template as the value. Finally, default value is provided.
 * 
 * @author ceyates
 * 
 * $Id$
 */
public class TemplateChooser {

    /** the name of the default template */
    private String defaultTemplate;

    /** a Map used internally that contains the compiled regular expressions */
    private Map<String, Pattern> patternMap;

    /** a Map of regular expressions associated with templates */
    private Map<String, String> templateMap = Collections.emptyMap();

    public TemplateChooser(final String defaultTemplate, final Map<String, String> templateMap) {
        if (null == defaultTemplate) {
            throw new IllegalArgumentException("null defaultTemplate");
        }
        if (null != templateMap) {
            this.templateMap = templateMap;
            this.patternMap = new HashMap<String, Pattern>(templateMap.size());
            for (String pattern : templateMap.keySet()) {
                this.patternMap.put(pattern, Pattern.compile(pattern));
            }
        }
        this.defaultTemplate = defaultTemplate;
    }

    public void setupTemplate(final HttpServletRequest request) {
        String template = request.getParameter("template");
        if (null == template && this.templateMap.size() > 0) {
            String uri = request.getRequestURI().substring(request.getContextPath().length());
            for (Entry<String, Pattern> entry : this.patternMap.entrySet()) {
                if (entry.getValue().matcher(uri).matches()) {
                    template = this.templateMap.get(entry.getKey());
                    break;
                }
            }
        }
        request.setAttribute("template", null == template ? this.defaultTemplate : template);
    }
}
