"use strict";

window.model = {"base-path":""};

Y.Test.Runner.add(new Y.Test.Case({

    name: "Search Help TestCase",
    
    "test searchTabs:change": function() {
        L.fire("searchTabs:change", {newVal:{source:"foo",foo:{help:"new href"}}});
        Y.Assert.areEqual("new href", document.querySelector(".search-help").getAttribute("href"));
    }

}));

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.masterSuite.name = "search-help-test.js";
Y.Test.Runner.run();
