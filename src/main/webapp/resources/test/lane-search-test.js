/**
 * @author ceyates
 */
YUI({ logInclude: { TestRunner: true } }).use('console','test', function(Y) {
    
    var searchTestCase = new Y.Test.Case({
        
        setUp: function() {
            this.initialText = Y.one('#searchTerms').get('value');
        },
        tearDown: function() {
            Y.one('#searchTerms').set('value', this.initialText);
        },
    
        testStartSearch: function() {
            var searchTermsInput = Y.one('#searchTerms');
            var searchIndicator = Y.one('#searchIndicator');
            Y.Assert.areEqual('hidden', searchIndicator.getStyle('visibility'));
            Y.Assert.isFalse(LANE.search.isSearching());
            searchTermsInput.set('value', 'hello');
            LANE.search.startSearch();
            Y.Assert.isTrue(LANE.search.isSearching());
            Y.Assert.areEqual('visible', searchIndicator.getStyle('visibility'));
            LANE.search.stopSearch();
            Y.Assert.areEqual('hidden', searchIndicator.getStyle('visibility'));
        },
        testIsSearching: function() {
            var searchTermsInput = Y.one('#searchTerms');
            searchTermsInput.set('value','foo');
            Y.Assert.isFalse(LANE.search.isSearching());
            LANE.search.startSearch();
            Y.Assert.isTrue(LANE.search.isSearching());
            LANE.search.stopSearch();
            Y.Assert.isFalse(LANE.search.isSearching());
            
        },
        testStartSearchNoQuery: function() {
            try {
                LANE.search.startSearch();
                Y.Assert.fail('should cause exception');
            } catch (ex) {
                Y.Assert.areEqual('nothing to search for', ex.toString());
            }
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
 
    
    Y.Global.on('lane:searchready', function() {
        Y.Test.Runner.add(searchTestCase);
        Y.Test.Runner.run();
    });
});
