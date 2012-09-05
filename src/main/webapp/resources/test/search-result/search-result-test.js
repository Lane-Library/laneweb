Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate','console','test', function(Y) {

    var resultTestCase = new Y.Test.Case({
        
        name: 'Lane Result Test Case',
        result: LANE.SearchResult,
        
        testGetSearchSource: function() {
            Y.Assert.areEqual('foo', this.result.getSearchSource());
        },
        testGetSearchTerms: function() {
            Y.Assert.areEqual('bar baz', this.result.getSearchTerms());
        },
        testGetEncodedSearchTerms: function() {
            Y.Assert.areEqual('bar+baz', this.result.getEncodedSearchTerms());
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
    
    Y.Test.Runner.add(resultTestCase);
    Y.Test.Runner.run();
});
