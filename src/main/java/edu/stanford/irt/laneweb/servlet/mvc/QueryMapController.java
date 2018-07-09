package edu.stanford.irt.laneweb.servlet.mvc;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import edu.stanford.irt.laneweb.eresources.SolrQueryParser;
import edu.stanford.irt.querymap.QueryMapException;
import edu.stanford.irt.querymap.QueryMapper;
import edu.stanford.irt.querymap.ResourceMap;

@Controller
public class QueryMapController {

    private static final String ERROR_MESSAGE_FORMAT = "QueryMapper failed to get resource map: %s";

    private static final Logger log = LoggerFactory.getLogger(QueryMapController.class);

    private SolrQueryParser parser;

    private QueryMapper queryMapper;

    public QueryMapController(final SolrQueryParser parser, final QueryMapper queryMapper) {
        this.parser = parser;
        this.queryMapper = queryMapper;
    }

    @RequestMapping(value = "/apps/querymap/json", method = RequestMethod.GET)
    @ResponseBody
    public ResourceMap getJSONResourceMap(@RequestParam final String q) {
        return this.queryMapper.getResourceMap(this.parser.parse(q));
    }

    @ExceptionHandler(QueryMapException.class)
    @ResponseBody
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(final QueryMapException exception) {
        String message = String.format(ERROR_MESSAGE_FORMAT, exception);
        log.error(message);
        return message;
    }
}
