package edu.stanford.irt.laneweb.search;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;


import org.springframework.util.Assert;
import org.apache.avalon.framework.parameters.Parameters;
import org.apache.cocoon.environment.ObjectModelHelper;
import org.apache.cocoon.environment.SourceResolver;

import edu.stanford.irt.search.Result;

public class EngineSearchGenerator extends SearchGenerator{

    private String[] e;

    
    public Result doSearch(){
      Collection<String> engines = new HashSet<String>();
      if ((this.e != null) && (this.e.length > 0)) {
          for (String element : this.e) {
              engines.add(element);
          }
      }   
      return super.doSearch(engines);
    }
    
    
    public void setup(final SourceResolver resolver, final Map objectModel, final String src, final Parameters par){
        super.setup(resolver, objectModel, src, par);
        HttpServletRequest request = ObjectModelHelper.getRequest(objectModel);
        this.e = request.getParameterValues("e"); 
        Assert.notNull(this.e, "engines cannot be null");
    }
}
