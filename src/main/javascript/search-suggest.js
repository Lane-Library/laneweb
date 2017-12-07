if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var lane = Y.lane,

        form = document.querySelector(".search-form"),

        queryInput = form.querySelector("input[name=q]"),

        model = function(suggest, source) {
            return {
                suggest: suggest,
                source: source
            };
        }(new lane.Suggest(new Y.Node(queryInput)),
                form.querySelector("input[name=source]").value),

        view = function() {

            return {
                search: function(query) {
                    queryInput.value = query;
                    form.submit();
                }
            };

        }(),

        controller = function() {

            return {
                sourceChange: function(event) {
                    // default suggest limit is mesh-di
                    var source = event.newVal,
                        limit = "";
                    if (source.match(/^all/)) {
                        limit = "er-mesh";
                    } else if (source.match(/^(bioresearch|images)/)) {
                        limit = "mesh";
                    } else if (source.match(/^bassett/)) {
                        limit = "Bassett";
                    }
                    model.suggest.setLimit(limit);
                    model.source = source;
                },
                suggestion: function(event) {
                    lane.fire("tracker:trackableEvent", {
                        category: "lane:suggestSelect",
                        action: model.source,
                        label: event.suggestion
                    });
                    view.search(event.suggestion);
                }
            };

        }();

        controller.sourceChange({newVal: model.source});

        model.suggest.on("suggest:select", controller.suggestion);

        lane.on("search:sourceChange", controller.sourceChange);
})();

}
