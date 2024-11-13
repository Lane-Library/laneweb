if (document.querySelector(".search-form")) {

    (function () {

        "use strict";

        let model = function (q, s) {

            let query = q,
                source = s,
                m = {
                    getQuery: function () {
                        return query;
                    },
                    getSource: function () {
                        return source;
                    },
                    search: function () {
                        if (query) {
                            this.fire("search");
                        }
                    },
                    setQuery: function (newQuery) {
                        let oldQuery = query;
                        if (typeof newQuery === "string") {
                            query = newQuery;
                            this.fire("queryChange", {
                                newVal: newQuery,
                                oldVal: oldQuery
                            });
                        }
                    },
                    setSource: function (newSource) {
                        let oldSource = source;
                        if (typeof newSource === "string") {
                            this.fire("sourceChange", {
                                newVal: newSource,
                                oldVal: oldSource
                            });
                            source = newSource;
                        }
                    }
                };

            L.addEventTarget(m, {
                prefix: "search"
            });


            m.addTarget(L);

            return m;

        }(document.querySelector(".search-form input[name=q]").value,
            document.querySelector(".search-form input[name=source]").value),

            view = function (form) {
                let queryInput = form.querySelector("input[name=q]"),
                    sourceInput = form.querySelector("input[name=source]"),
                    facetsInput = form.querySelector("input[name=facets]"),
                    sortInput = form.querySelector("input[name=sort]"),

                    v = {
                        close: function () {
                            document.documentElement.scrollIntoView();
                            view.fire("close");
                        },
                        inputChange: function () {
                            view.fire("inputChange", queryInput.value);
                        },
                        open: function () {
                            view.fire("open");
                        },
                        reset: function () {
                            facetsInput.disabled = "disabled";
                            sortInput.disabled = "disabled";
                        },
                        search: function () {
                            form.submit();
                        },
                        submit: function (event) {
                            view.fire("submit", event);
                        },
                        updateQuery: function (query) {
                            if (query !== queryInput.value) {
                                queryInput.value = query;
                            }
                        },
                        updateSource: function (source) {
                            sourceInput.value = source;
                            if (facetsInput.value) {
                                facetsInput.value = '';
                                facetsInput.disabled = "disabled";
                            }
                        }
                    };


                form.addEventListener("submit", v.submit);

                queryInput.addEventListener("focus", v.open);
                queryInput.addEventListener("input", v.inputChange);

                L.addEventTarget(v);

                return v;

            }(document.querySelector(".search-form"));

        (function () {
            let controller = {
                open: function () {
                    model.fire("activeChange", { active: true });
                },
                close: function () {
                    model.fire("activeChange", { active: false });
                },
                submit: function (event) {
                    event.preventDefault();
                    model.search();
                },
                inputChange: function (input) {
                    model.setQuery(input);
                },
                search: function () {
                    view.search();
                },
                reset: function () {
                    model.setQuery("");
                    view.reset();
                },
                searchDropdownChange: function (event) {
                    model.setSource(event.newVal);
                    model.search();
                },
                queryChange: function (event) {
                    view.updateQuery(event.newVal);
                },
                sourceChange: function (event) {
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

            L.on("searchDropdown:change", controller.searchDropdownChange);
            L.on("searchReset:reset", controller.reset);
        })();

        L.search = model;

    })();

}
