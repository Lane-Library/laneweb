if (document.querySelector(".search-form")) {

    (function () {

        "use strict";

        let form = document.querySelector(".search-form"),
            queryInput = form.querySelector("input[name=q]"),
            suggest = new L.Suggest(queryInput),

            model = function (suggest, source) {
                return {
                    suggest: suggest,
                    source: source
                };
            }(suggest,
                form.querySelector("input[name=source]").value),

            view = function () {

                return {
                    search: function (query) {
                        queryInput.value = query;
                        form.submit();
                    }
                };

            }(),

            controller = function () {
                return {
                    sourceChange: function (event) {
                        // default suggest limit is mesh-di
                        let source = event.newVal,
                            limit;
                        if (source.match(/^(all|catalog)/)) {
                            limit = "er-mesh";
                        } else {
                            limit = "";
                        }
                        model.suggest.setLimit(limit);
                        model.source = source;
                    },
                    suggestion: function (event) {
                        L.fire("tracker:trackableEvent", {
                            category: "lane:suggestSelect",
                            action: model.source,
                            label: event.suggestion
                        });
                        view.search(event.suggestion);
                    }
                };

            }();

        controller.sourceChange({ newVal: model.source });
        L.addEventTarget(model.suggest);
        model.suggest.on("suggest:select", controller.suggestion);

        L.on("search:sourceChange", controller.sourceChange);
    })();

}
