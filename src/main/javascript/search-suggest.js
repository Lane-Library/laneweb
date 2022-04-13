if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var form = document.querySelector(".search-form"),

        queryInput = form.querySelector("input[name=q]"),

        model = function(suggest, source) {
            return {
                suggest: suggest,
                source: source
            };
        }(new L.Suggest(queryInput),
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
                        limit;
                    if (source.match(/^(all|catalog)/)) {
                        limit = "er-mesh";
                    } else if (source.match(/^images/)) {
                        limit = "mesh";
                    } else {
                        limit = "";
                    }
                    model.suggest.setLimit(limit);
                    model.source = source;
                },
                suggestion: function(event) {
                    L.fire("tracker:trackableEvent", {
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

        L.on("search:sourceChange", controller.sourceChange);
})();

}
