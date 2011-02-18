package edu.stanford.irt.laneweb.servlet.mvc;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import edu.stanford.irt.querymap.QueryMap;
import edu.stanford.irt.querymap.QueryMapper;

@Controller
public class QueryMapController {

    @Autowired
    private QueryMapper queryMapper;
    
    @RequestMapping(value = "/apps/querymap/json")
    @ResponseBody
    public QueryMap getQueryMap(@RequestParam String q) {
        QueryMap queryMap = this.queryMapper.getQueryMap(q);
        QueryMap result = new QueryMap(q, queryMap.getDescriptor(), queryMap.getResourceMap(), null, null);
        return result;
    }
}
