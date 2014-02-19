(function() {

    Y.namespace("lane");

    var lane = Y.lane,
        model = lane.Model,
        basePath = model.get(model.BASE_PATH) || "",
        SEARCH_INTERVAL = 2000,

    ResourceSearch = {

        search: function(query, resources) {
            var i, resourcesParams = "";
            for (i = 0; i < resources.length; i++) {
                resourcesParams += "&r=" + resources[i];
            }
            Y.io(basePath + "/apps/search/json?q=" + query + resourcesParams, {
                on : {
                    success : function(id, o) {
                        var result = Y.JSON.parse(o.responseText);
                        this.fire("update", result);
                        if (result.status === "running") {
                            Y.later(SEARCH_INTERVAL, window, function() {
                                ResourceSearch.search(query, resources);
                            });
                        }
                    }
                },
                context : this
            });
        }

    },

    ResourceResultView = function(id, fn) {
        this._handle = ResourceSearch.on("update", function(result) {
            var resourceResult = result.resources[id];
            fn.call(resourceResult);
            if (resourceResult.status) {
                this._handle.detach();
            }
        }, this);
    };

    Y.augment(ResourceSearch, Y.EventTarget);

    lane.ResourceSearch = ResourceSearch;

    lane.ResourceResultView = ResourceResultView;

})();