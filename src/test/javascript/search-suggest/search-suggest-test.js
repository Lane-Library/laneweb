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
        L.fire("search:sourceChange", {newVal: "images-all"});
        Y.Assert.areEqual("mesh", this.suggest.limit);
        L.fire("search:sourceChange", {newVal: "bassett"});
        Y.Assert.areEqual("Bassett", this.suggest.limit);
        L.fire("search:sourceChange", {newVal: "all-all"});
        Y.Assert.areEqual("er-mesh", this.suggest.limit);
        L.fire("search:sourceChange", {newVal: "bioresearch-all"});
        Y.Assert.areEqual("mesh", this.suggest.limit);
        L.fire("search:sourceChange", {newVal: "textbooks-all"});
        Y.Assert.areEqual("", this.suggest.limit);
    },

    "test suggest:select event": function() {
        this.suggest.fire("suggest:select", {suggestion:"suggestion"});
        Y.Assert.isTrue(window.submitted);
    }

}));

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.masterSuite.name = "search-suggest-test.js";
Y.Test.Runner.run();
