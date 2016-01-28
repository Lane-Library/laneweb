"use strict";

Y.lane.Model.set(Y.lane.Model.URL_ENCODED_QUERY,"foo%bar");
Y.io = function(url, config) {
    config.on.success.apply(this, [1,{responseText:Y.JSON.stringify({results:{status:"successful",facets:{foo:{hits:0,status:"successful"}}}})}]);


    var searchFacetCountsTestCase = new Y.Test.Case({
        name: 'Lane search-facet-counts Test Case',

        testUpdated: function() {
            var facet = Y.one("#fooFacet");
            Y.Assert.isTrue(facet.one("a") === null);
            Y.Assert.isTrue(facet.hasClass("inactiveFacet"));
        }

    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(searchFacetCountsTestCase);
    Y.Test.Runner.masterSuite.name = "search-facet-counts-test.js";
    Y.Test.Runner.run();
};
