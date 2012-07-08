package edu.stanford.irt.laneweb.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;

@Controller
public class QueryMapController {
    
    private final Logger log = LoggerFactory.getLogger("querymap");

    @Autowired
    private QueryMapper queryMapper;

    @RequestMapping(value = "**/apps/querymap/json")
    @ResponseBody
    public QueryMap getJSONQueryMap(@RequestParam final String q) {
        QueryMap queryMap = this.queryMapper.getQueryMap(q);
        log.info("query=" + q + " descriptor=" + queryMap.getDescriptor() + " resourceMap=" + queryMap.getResourceMap());
        return new QueryMap(q, null, queryMap.getResourceMap(), null, null);
    }

    @RequestMapping(value = "**/apps/querymap.xml", produces = "text/xml")
    @ResponseBody
    public QueryMap getXMLQueryMap(@RequestParam final String q) {
        return this.queryMapper.getQueryMap(q);
    }
}
