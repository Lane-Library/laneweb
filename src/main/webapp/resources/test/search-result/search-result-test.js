Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate','console','test', function(Y) {

    var resultTestCase = new Y.Test.Case({
        
        name: 'Lane Result Test Case',
        
        result: Y.lane.SearchResult,
        
        testGetSearchSource: function() {
            Y.Assert.areEqual('foo', this.result.getSearchSource("source=foo&q=bar+baz"));
        },
        testGetSearchTerms: function() {
            Y.Assert.areEqual('bar baz', this.result.getSearchTerms("source=foo&q=bar+baz"));
        },
        testEncodeGetSearchTerms: function() {
            Y.Assert.areEqual('bar%20baz', encodeURIComponent(this.result.getSearchTerms("source=foo&q=bar+baz")));
        },
        testGetSearchSourceUndefined: function() {
        	Y.Assert.isUndefined(this.result.getSearchSource());
        },
        testGetSearchTermsUndefined: function() {
        	Y.Assert.isUndefined(this.result.getSearchTerms());
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
    
    Y.Test.Runner.add(resultTestCase);
    Y.Test.Runner.run();
});
