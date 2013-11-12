(function() {
	
	var model = Y.lane.Model,
	    query = model.get(model.QUERY),
	    encodedQuery = model.get(model.URL_ENCODED_QUERY),
	    queryMapper = Y.lane.QueryMapper,
	    resourceSearch = Y.lane.ResourceSearch,
	    ResourceResultView = Y.lane.ResourceResultView,
	    resources,
	    queryMapping = Y.one('#queryMapping');
        if (query && queryMapping) {
        	queryMapper.on("success", function(queryMap) {
                var i, span, labels = "",
                    resourceNames = [],
                    resourceMap = queryMap.resourceMap;
                if (resourceMap) {
                	resources = resourceMap.resources;
                    for (i = 0; i < resources.length; i++) {
                    	resourceNames.push(resources[i].id);
                        span = Y.Node.create('<span><a title="QueryMapping: ' + resources[i].label + '">' + resources[i].label + "</a></span>");
                        queryMapping.append(span);
                        (new ResourceResultView(resources[i].id, function() {
                        	span.one("a").set("href", this.url);
                        	if (this.status === "successful") {
                            	span.append(": " + this.hits + " ");
                        	}
                        }));
                        if (labels) {
                        	labels += "; ";
                        }
                        labels += resources[i].label;
                    }
                    resourceSearch.once("update", function() {
                    	Y.fire("lane:popin", queryMapping);
                    });
                    resourceSearch.search(encodedQuery, resourceNames);
                }

                // track mapped term, descriptor, and resources
                Y.lane.fire("tracker:trackableEvent", {
                    category: "lane:queryMapping",
                    action: "query=" + query + "; descriptor=" + queryMap.resourceMap.descriptor.descriptorName,
                    label: "resources=" + labels
                });
        	});
        	queryMapper.getQueryMap(encodedQuery);
        }
})();

