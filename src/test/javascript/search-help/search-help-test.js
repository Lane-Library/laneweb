YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    window.model = {"base-path":""};

    Y.Test.Runner.add(new Y.Test.Case({
        
        
        linkHelp : document.querySelector(".search-help a"),

        name: "Search Help TestCase",

        "test searchDropdown:change": function() {
            L.fire("searchDropdown:change", {newVal:{source:"foo",foo:{help:"new href"}}});
            Y.Assert.areEqual("new href", this.linkHelp.getAttribute("href"));
        }

    }));

    new Y.Test.Console().render();

    Y.Test.Runner.masterSuite.name = "search-help-test.js";
    Y.Test.Runner.run();

});
