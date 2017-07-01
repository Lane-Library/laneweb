package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.ResourceMap;

@Controller
public class QueryMapController {

    private SolrQueryParser parser;

    private QueryMapper queryMapper;

    @Autowired
    public QueryMapController(final SolrQueryParser parser, final QueryMapper queryMapper) {
        this.parser = parser;
        this.queryMapper = queryMapper;
    }

    @RequestMapping(value = "/apps/querymap/json")
    @ResponseBody
    public ResourceMap getJSONResourceMap(@RequestParam final String q) {
        return this.queryMapper.getResourceMap(this.parser.parse(q));
    }
}
