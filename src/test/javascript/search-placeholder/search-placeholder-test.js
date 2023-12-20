YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let placeholderEnabled = document.createElement("input").placeholder !== undefined;

    Y.Test.Runner.add(new Y.Test.Case({

        name: "Search Placeholder TestCase",

        input: document.querySelector("input"),

        "test initially has default": function() {
            if (placeholderEnabled) {
                Y.Assert.areEqual("default placeholder", this.input.placeholder);
            }  else {
                Y.Assert.areEqual("default placeholder", this.input.value);
                Y.Assert.areEqual("inputHint", this.input.className);
            }
        },

        "test tab change": function() {
            L.fire("searchDropdown:change", {newVal:{source:"clinical-all", "clinical-all":{placeholder:"new placeholder"}}});
            if (placeholderEnabled) {
                Y.Assert.areEqual("new placeholder", this.input.placeholder);
            }  else {
                Y.Assert.areEqual("new placeholder", this.input.value);
                Y.Assert.areEqual("inputHint", this.input.className);
            }
        },

        "test active change true": function() {
            L.fire("search:activeChange", {active:true});
            if (placeholderEnabled) {
                Y.Assert.areEqual("new placeholder", this.input.placeholder);
            }  else {
                Y.Assert.areEqual("new placeholder", this.input.value);
                Y.Assert.areEqual("inputHint", this.input.className);
            }
        },

        "test active change false": function() {
            L.fire("search:activeChange", {active:false});
            if (placeholderEnabled) {
                Y.Assert.areEqual("default placeholder", this.input.placeholder);
            }  else {
                Y.Assert.areEqual("default placeholder", this.input.value);
                Y.Assert.areEqual("inputHint", this.input.className);
            }
        }
    }));

    new Y.Test.Console().render();

    Y.Test.Runner.masterSuite.name = "search-placeholder-test.js";
    Y.Test.Runner.run();

});