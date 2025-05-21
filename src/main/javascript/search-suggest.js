

(function () {

    "use strict";

    let form = document.querySelector(".search-form"),
        sourceBase = '/apps/suggest/getSuggestionList?q={query}&l=';

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
                            suggestEndpoint;
                        // suggest-limit overrides source
                        if (suggestLimitInput) {
                            suggestEndpoint = sourceBase + suggestLimitInput.value;
                        }
                        else if (source.match(/^(all|catalog)/)) {
                            suggestEndpoint = sourceBase + "er-mesh";
                        } else {
                            suggestEndpoint = sourceBase + "mesh";
                        }
                        model.suggest.setSourceEndpoint(suggestEndpoint);
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


    }
})();

