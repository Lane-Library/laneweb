YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

"use strict";

window.submitted = false;
document.querySelector(".search-form").submit = function() {
    window.submitted = true;
};

Y.Test.Runner.add(new Y.Test.Case({

    name: "Search Suggest TestCase",

    suggest: null,
    
    setUp: function() {
        this.suggest = new L.Suggest();
        window.submitted = false;
    },
    "test search:sourceChange event": function() {
        L.fire("search:sourceChange", {newVal: "all-all"});
        Y.Assert.areEqual("er-mesh", this.suggest.limit);
        L.fire("search:sourceChange", {newVal: "catalog-all"});
        Y.Assert.areEqual("er-mesh", this.suggest.limit);
    },

    "test suggest:select event": function() {
        this.suggest.fire("suggest:select", {suggestion:"suggestion"});
        Y.Assert.isTrue(window.submitted);
    }

}));

new Y.Test.Console().render();

Y.Test.Runner.masterSuite.name = "search-suggest-test.js";
Y.Test.Runner.run();

});
