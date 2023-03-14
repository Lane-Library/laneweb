package edu.stanford.irt.laneweb.trends.googleA4;

import java.util.HashMap;
import java.util.Map;

public class Event {
    
    String name;
    
    Map<String, Object> params;
    
    
    public Event(String name) {
        this.name = name;
        this.params = new HashMap<String, Object>();
    }
    
    public void addParamters(String key, Object value) {
        this.params.put(key, value);
    }
    
   
}
