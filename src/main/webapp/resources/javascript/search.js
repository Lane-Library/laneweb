if (document.querySelector(".search-form"))  {

(function() {

    "use strict";

    var lane = Y.lane,
        model = function(query, source) {
            var m = {
                    getQuery: function() {
                        return query;
                    },
                    getSource: function() {
                        return source;
                    },
                    search: function() {
                        if (query) {
                            this.fire("search");
                        }
                    },
                    setQuery: function(q) {
                        var oldVal = query;
                        if (typeof q === "string") {
                            query = q;
                            this.fire("queryChange", {
                                newVal: q,
                                oldVal: oldVal
                            });
                        }
                    },
                    setSource: function(s) {
                        var oldVal = source;
                        if (typeof s === "string") {
                            this.fire("sourceChange", {
                                newVal: s,
                                oldVal: oldVal
                            });
                        }
                    }
                };

            Y.augment(m, Y.EventTarget, false, null, {
                prefix: "search",
                emitFacade: true
            });

            m.publish("sourceChange", {
                defaultFn: function(event) {
                    source = event.newVal;
                }
            });
            m.addTarget(lane);

            return m;

        }(document.querySelector(".search-form input[name=q]").value,
                document.querySelector(".search-form input[name=source]").value),

        view = function(form) {
            var queryInput = form.querySelector("input[name=q]"),
                sourceInput = form.querySelector("input[name=source]"),
                facetsInput = form.querySelector("input[name=facets]"),
                sortInput = form.querySelector("input[name=sort]"),
                queryNode = new Y.Node(queryInput),
                queryTextInput = new lane.TextInput(queryNode, queryInput.getAttribute("placeholder")),

                view = {
                    close: function() {
                        lane.deactivate(form, "search-form");
                        new Y.Anim({
                            node: "win",
                            to: { scroll: [0, 0] },
                            duration: 0.3,
                            easing: Y.Easing.easeBoth
                        }).run();
                        view.fire("close");
                    },
                    inputChange: function() {
                        view.fire("inputChange", queryTextInput.getValue());
                    },
                    open: function() {
                        lane.activate(form, "search-form");
                        view.fire("open");
                    },
                    reset: function() {
                        facetsInput.disabled = "disabled";
                        sortInput.disabled = "disabled";
                    },
                    search: function() {
                        form.submit();
                    },
                    submit: function(event) {
                        view.fire("submit", event);
                    },
                    updateQuery: function(query) {
                        if (query !== queryTextInput.getValue()) {
                            queryTextInput.setValue(query);
                        }
                    },
                    updateSource: function(source) {
                        sourceInput.value = source;
                    }
                };

            // scroll to form if there is an initial query and not already at /search.html
            if (/\/search\.html/.test(location.pathname) && queryTextInput.getValue() && !/\/search\.html/.test(document.referrer)) {
                Y.once("domready", function() {
                    new Y.Anim({
                        node: "win",
                        to: { scroll: [0, form.offsetTop + 50] },
                        duration: 0.3,
                        easing: Y.Easing.easeBoth
                    }).run();
                });
            }

            form.querySelector(".search-close").addEventListener("click", view.close);
            form.addEventListener("submit", view.submit);

            queryInput.addEventListener("focus", view.open);
            if (Y.UA.ie === 9) {
                queryInput.addEventListener("change", view.inputChange);
                queryInput.addEventListener("keyup", view.inputChange);
                queryInput.addEventListener("paste", view.inputChange);
            } else {
                queryInput.addEventListener("input", view.inputChange);
            }

            Y.augment(view, Y.EventTarget);

            return view;

        }(document.querySelector(".search-form"));

        (function(model, view) {
            var controller = {
                    open: function() {
                        model.fire("activeChange", {active:true});
                    },
                    close: function() {
                        model.fire("activeChange", {active:false});
                    },
                    submit: function(event) {
                        event.preventDefault();
                        model.search();
                    },
                    inputChange: function(input) {
                        model.setQuery(input);
                    },
                    search: function() {
                        view.search();
                    },
                    reset: function() {
                        model.setQuery("");
                        view.reset();
                    },
                    searchTabsChange: function(event) {
                        model.setSource(event.newVal.source);
                        model.search();
                    },
                    queryChange: function(event) {
                        view.updateQuery(event.newVal);
                    },
                    sourceChange: function(event) {
                        view.updateSource(event.newVal);
                    }
                };

            model.on("queryChange", controller.queryChange);
            model.on("sourceChange", controller.sourceChange);
            model.on("search", controller.search);

            view.on("submit", controller.submit);
            view.on("inputChange", controller.inputChange);
            view.on("open", controller.open);
            view.on("close", controller.close);

            lane.on("searchTabs:change", controller.searchTabsChange);
            lane.on("searchReset:reset", controller.reset);
        })(model, view);

    lane.search = model;

})();

}