
(function () {

    "use strict";

    let form = document.querySelector(".search-form");

    // table search inputs (e.g. course reserves, liaisons, equipment) should not get solr suggestions: LANEWEB-11444
    if (form && form.querySelector("input[name=q]:not(#table-search-input)")) {

        let queryInput = form.querySelector("input[name=q]"),
            model = function (suggest, source) {
                return {
                    suggest: suggest,
                    source: source
                };
            }(new L.Suggest(queryInput),
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
                        let source = event.newVal,
                            suggestLimitInput = form.querySelector("input[name=suggest-limit]"),
                            limit;
                        // suggest-limit overrides source
                        if (suggestLimitInput) {
                            limit = suggestLimitInput.value;
                        }
                        else if (source.match(/^(all|catalog)/)) {
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

        model.suggest.on("suggest:select", controller.suggestion);

        L.on("search:sourceChange", controller.sourceChange);
    }
})();


