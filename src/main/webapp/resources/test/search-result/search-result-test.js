/**
 * @author ceyates
 */
if (!window.location.search) {
    window.location = window.location + '?source=foo&q=bar+baz';
}
YUI({ logInclude: { TestRunner: true } }).use('node-event-simulate','console','test', function(T) {
    
    var resultTestCase = new T.Test.Case({
        
        name: 'Lane Result Test Case',
        result: Y.lane.SearchResult,
        
        testGetSearchSource: function() {
            T.Assert.areEqual('foo', this.result.getSearchSource());
        },
        testGetSearchTerms: function() {
            T.Assert.areEqual('bar baz', this.result.getSearchTerms());
        },
        testGetEncodedSearchTerms: function() {
            T.Assert.areEqual('bar+baz', this.result.getEncodedSearchTerms());
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false                   
    }).render('#log');
    
    T.Test.Runner.add(resultTestCase);
    T.Test.Runner.run();
});
