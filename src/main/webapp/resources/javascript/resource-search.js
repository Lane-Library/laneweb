(function() {
    
    Y.namespace("lane");
    
    var lane = Y.lane,
        model = lane.Model,
        basePath = model.get(model.BASE_PATH) || "",
    
    ResourceSearch = {

	    search: function(query, resources) {
	    	var i, resourcesParams = "";
	    	for (i = 0; i < resources.length; i++) {
	    		resourcesParams += "&r=" + resources[i];
	    	}
            Y.io(basePath + "/apps/search/json?q=" + query + resourcesParams, {
                on : {
                    success : function(id, o) {
                        this.fire("success", Y.JSON.parse(o.responseText));
                    }
                },
                context : this
            });
        }
    
    };

    Y.augment(ResourceSearch, Y.EventTarget);
    
    lane.ResourceSearch = ResourceSearch;
	
})();