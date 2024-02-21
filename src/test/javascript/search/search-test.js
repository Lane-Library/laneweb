YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let submitted = false,

    searchTestCase = {

            name: "Lane Search Test Case",

            search: L.search,

            form: Y.one(".search-form"),

            queryInput: Y.one("input[name=q]"),

            sourceInput: Y.one("input[name=source]"),

            close: Y.one(".search-close"),

            setUp: function() {
                this.search.setSource("all-all");
                this.search.setQuery("");
                submitted = false;
            },

            testExists: function() {
                Y.Assert.isTrue(Y.Lang.isObject(this.search));
            },

            testSetGetQuery: function() {
                this.search.setQuery("query");
                Y.Assert.areEqual("query", this.search.getQuery());
                Y.Assert.areEqual("query", this.queryInput.get("value"));
            },

            testQueryChangeEvent: function() {
                let value;
                this.search.once("queryChange", function() {
                    value = this.getQuery();
                });
                this.search.setQuery("query");
                Y.Assert.areEqual("query", value);
            },

            testQueryChangeEventBubble: function() {
                let newVal, oldVal;
                L.once("search:queryChange", function(event) {
                    newVal = event.newVal;
                    oldVal = event.oldVal;
                });
                this.search.setQuery("query");
                Y.Assert.areEqual("", oldVal);
                Y.Assert.areEqual("query", newVal);
            },

            testSetQueryNull: function() {
                this.search.setQuery("query");
                this.search.setQuery(null);
                Y.Assert.areEqual("query", this.search.getQuery());
                Y.Assert.areEqual("query", this.queryInput.get("value"));
            },

            testSetQueryUndefined: function() {
                this.search.setQuery("query");
                this.search.setQuery(this.foo);
                Y.Assert.areEqual("query", this.search.getQuery());
                Y.Assert.areEqual("query", this.queryInput.get("value"));
            },

            testSetGetSource: function() {
                this.search.setSource("clinical-all");
                Y.Assert.areEqual("clinical-all", this.search.getSource());
                Y.Assert.areEqual("clinical-all", this.sourceInput.get("value"));
            },

            testSourceChangeEvent: function() {
                let value;
                this.search.once("sourceChange", function(event) {
                    value = event.newVal;
                });
                this.search.setSource("clinical-all");
                Y.Assert.areEqual("clinical-all", value);
            },

            testSourceChangeEventBubble: function() {
                let newVal, oldVal;
                L.once("search:sourceChange", function(event) {
                    newVal = event.newVal;
                    oldVal = event.oldVal;
                });
                this.search.setSource("clinical-all");
                Y.Assert.areEqual("all-all", oldVal);
                Y.Assert.areEqual("clinical-all", newVal);
            },

            "test searchDropdown:change event": function() {
                L.fire("searchDropdown:change", {newVal: {source:"clinical-all"}});
                Y.Assert.areEqual("clinical-all", this.search.getSource());
                Y.Assert.areEqual("clinical-all", this.sourceInput.get("value"));
            },

            testSetSourceNull: function() {
                this.search.setSource("all-all");
                this.search.setSource(null);
                Y.Assert.areEqual("all-all", this.search.getSource());
                Y.Assert.areEqual("all-all", this.sourceInput.get("value"));
            },

            testSetSourceUndefined: function() {
                this.search.setSource("all-all");
                this.search.setSource(this.foo);
                Y.Assert.areEqual("all-all", this.search.getSource());
                Y.Assert.areEqual("all-all", this.sourceInput.get("value"));
            },

            testSearch: function() {
                let searched = false;
                this.search.once("search", function(event) {
                    searched = true;
                });
                this.search.setQuery("query");
                this.search.search();
                Y.Assert.isTrue(searched);
                Y.Assert.isTrue(submitted);
            },

            testSearchNoQuery: function() {
                let searched = false;
                this.search.once("search", function(e) {
                    searched = true;
                });
                this.search.search();
                Y.Assert.isFalse(searched);
                Y.Assert.isFalse(submitted);
            },

            testSearchEventBubble: function() {
                let searched = false;
                L.once("search:search", function(event) {
                    searched = true;
                });
                this.search.setQuery("query");
                this.search.search();
                Y.Assert.isTrue(searched);
                Y.Assert.isTrue(submitted);
            },

            testInputFocus: function() {
                this.queryInput.simulate("focus");
            },

            testCloseClick: function() {
                this.close.simulate("click");
            },

            testSubmit: function() {
                let searched = false;
                this.search.once("search", function(event) {
                    searched = true;
                });
                this.search.setQuery("query");
                Y.one("form").simulate("submit");
                Y.Assert.isTrue(searched);
                Y.Assert.isTrue(submitted);
            },

            testSubmitNoQuery: function() {
                let searched = false;
                this.search.once("search", function(event) {
                    searched = true;
                });
                Y.one("form").simulate("submit");
                Y.Assert.isFalse(searched);
                Y.Assert.isFalse(submitted);
            },

            testInputChange: function() {
                this.queryInput.set("value", "query");
                let e = document.createEvent("UIEvents");
                e.initEvent("input", true, false);
                this.queryInput._node.dispatchEvent(e);
                Y.Assert.areEqual("query", this.search.getQuery());
            },

            "test reset clears query": function() {
                this.search.setQuery("query");
                Y.Assert.areEqual("query", this.queryInput.get("value"));
                L.fire("searchReset:reset");
                Y.Assert.areEqual("", this.search.getQuery());
            },

            "searchDropdown:change submits if query": function() {
                this.search.setQuery("query");
                let searched = false;
                this.search.once("search", function(event) {
                    searched = true;
                });
                Y.Assert.areEqual("query", this.queryInput.get("value"));
                L.fire("searchDropdown:change", {newVal: {source:"foo"}});
                Y.Assert.isTrue(searched);
                Y.Assert.isTrue(submitted);
            },

            "searchDropdown:change doesn't submit if no query": function() {
                this.search.setQuery("");
                let searched = false;
                this.search.once("search", function(event) {
                    searched = true;
                });
                Y.Assert.areEqual("", this.queryInput.get("value"));
                L.fire("searchDropdown:change", {newVal: {source:"foo"}});
                Y.Assert.isFalse(searched);
                Y.Assert.isFalse(submitted);
            }
    };

    document.querySelector(".search-form").submit = function() {
        submitted = true;
        console.log("/search.html?q=" + encodeURIComponent(L.search.getQuery()) + "&source=" + L.search.getSource());
    };

    new Y.Test.Console().render();

    Y.Test.Runner.add(new Y.Test.Case(searchTestCase));
    Y.Test.Runner.masterSuite.name = "search-test.js";
    Y.Test.Runner.run();

});
