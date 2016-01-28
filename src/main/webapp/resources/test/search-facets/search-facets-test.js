"use strict";

var searchFacetsTestCase = new Y.Test.Case({
    name: 'Lane Search Facets Test Case',

    testFacetClick: function() {
        Y.one("#b-bFacet").simulate("click");
        Y.Assert.areEqual("/plain/search/b/b-b.html?source=b-b&amp;q=undefined", Y.one("#searchResults").get("innerHTML"));
    },

    testAnotherFacetClick: function() {
        Y.one("#c-cFacet").simulate("click");
        Y.Assert.areEqual("/plain/search/c/c-c.html?source=c-c&amp;q=undefined", Y.one("#searchResults").get("innerHTML"));
    },

    testYetAnotherFacetClick: function() {
        Y.one("#a-aFacet").simulate("click");
        Y.Assert.areEqual("a-a results", Y.one("#searchResults").get("innerHTML"));
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(searchFacetsTestCase);
Y.Test.Runner.masterSuite.name = "search-facets-test.js";
Y.Test.Runner.run();
