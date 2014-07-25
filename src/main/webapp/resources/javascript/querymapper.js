(function(){

    Y.namespace("lane");

    var lane = Y.lane,
        model = lane.Model,
        basePath = model.get(model.BASE_PATH) || "",

    QueryMapper = {

        getQueryMap: function(query) {
            Y.io(basePath + "/apps/querymap/json?q=" + query, {
                on : {
                    success : function(id, o) {
                        this.fire("success", Y.JSON.parse(o.responseText));
                    }
                },
                context : this
            });
        }

    };

    Y.augment(QueryMapper, Y.EventTarget);

    lane.QueryMapper = QueryMapper;

})();