package edu.stanford.irt.laneweb.servlet.mvc.search;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import edu.stanford.irt.search.impl.ContentResult;
import edu.stanford.irt.search.impl.MetaSearchManager;
import edu.stanford.irt.search.impl.Result;
import edu.stanford.irt.search.impl.SimpleQuery;

@Controller
@RequestMapping(value = "/mvc/search/")
public class ImagesSearchController {

    @Autowired
    MetaSearchManager metasearchManager;
//    @RequestMapping(value = "/searchImages.html")
     @RequestMapping(value = "images.html")
     public String searchImgae(Model model, String q){//,@PathVariable String engines, @PathVariable String query){
         Result searchResult = null;
         String engines = "elasticsearch-endoatlas,wellcomeimages,philcdc_photos";
            if (q == null || q.isEmpty()) {
                searchResult = new Result("");
            } else {
                searchResult = this.metasearchManager.search(new SimpleQuery(q, Arrays.asList(engines.split(","))), 30000L , true);
            }
         List<ContentResult> result = new LinkedList<>();
         Collection<Result> enginesResult = searchResult.getChildren();
         for (Result engine : enginesResult) {
            Collection<Result> resources = engine.getChildren();
            for (Result resourceResult : resources) {
                if(resourceResult.getId() != null && resourceResult.getId().contains("_content")){
                    for (Result content : resourceResult.getChildren()) {
                        result.add((ContentResult) content);
                    }
                }
            }
        }
         model.addAttribute("searchResult", result);
         
         return "imagesSearch";
     }
    

}
