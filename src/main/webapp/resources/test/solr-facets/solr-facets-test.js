/**
 * @author ryanmax
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    Y.lane.Model.set(Y.lane.Model.URL_ENCODED_QUERY, "foo");

    Y.io = function(url, config) {
        config.on.success.apply(config.context, [0, {responseText:'<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Strict//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-strict.dtd"><html xmlns="http://www.w3.org/1999/xhtml"><body><div class="bd"><h3>Filter Results</h3><ul><li class="solrFacet facetHeader">Results from <span id="sources"><i class="fa fa-info-circle"></i></span></li><li class="enabled"><a href="?source=all-all&amp;q=foo" title="remove"><i class="fa fa-check-square fa-lg"></i></a><span class="facetLabel">Lane Catalog</span><span class="facetCount">9</span></li></ul></div></body></html>'}, {}]);
    };

    var solrFacetsTestCase = new Y.Test.Case({
        name: "Solr Facets TestCase",
        testBasic: function() {
            var solrFacets = Y.one('.solrFacets');
            Y.Assert.areEqual(2, solrFacets.all("li").size());
            Y.Assert.areEqual(1, Y.one('#solrLimits').all(".clearLimits").size());
        }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(solrFacetsTestCase);
    Y.Test.Runner.masterSuite.name = "solr-facets-test.js";
    Y.Test.Runner.run();
});
