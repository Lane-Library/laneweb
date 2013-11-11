/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y){
    
    Y.io = function(url, config) {
        config.on.success.apply(this, [0,{responseText:'{"status":"successful"}'}]);
    };

    var resourceSearchTestCase = new Y.Test.Case({
        name: 'Lane ResourceSearch Test Case',
        
        search: Y.lane.ResourceSearch,
        
        view: new Y.lane.ResourceResultView(0, this.foo),
        
        foo: function(id, result) {
            this.result = result;
        },
        
        testSearch: function() {
            this.search.search("query", ["pubmed","scopus"]);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(resourceSearchTestCase);
    Y.Test.Runner.masterSuite.name = "resource-search-test.js";
    Y.Test.Runner.run();
});
