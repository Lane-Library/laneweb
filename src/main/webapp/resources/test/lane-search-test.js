/**
 * @author ceyates
 */
YUI({ logInclude: { TestRunner: true } }).use('lane-search','node-event-simulate','console','test', function(Y) {
    
    var searchTestCase = new Y.Test.Case({
		
		name: 'Lane Search Test Case',
		
        searchTermsInput: Y.one('#searchTerms'),
        searchIndicator: Y.one('#searchIndicator'),
		searchSource: Y.one('#searchSource'),
        
        setUp: function() {
            this.searchTermsInput.set('value', '');
            this.searchTermsInput.set('title', '');
			this.searchSource.set('selectedIndex',0);
        },
    
        testStartSearch: function() {
            Y.Assert.areEqual('hidden', this.searchIndicator.getStyle('visibility'));
            Y.Assert.isFalse(LANE.search.isSearching());
            this.searchTermsInput.set('value', 'hello');
            LANE.search.startSearch();
            Y.Assert.isTrue(LANE.search.isSearching());
            Y.Assert.areEqual('visible', this.searchIndicator.getStyle('visibility'));
            LANE.search.stopSearch();
            Y.Assert.areEqual('hidden', this.searchIndicator.getStyle('visibility'));
        },
        testIsSearching: function() {
            this.searchTermsInput.set('value','foo');
            Y.Assert.isFalse(LANE.search.isSearching());
            LANE.search.startSearch();
            Y.Assert.isTrue(LANE.search.isSearching());
            LANE.search.stopSearch();
            Y.Assert.isFalse(LANE.search.isSearching());
            
        },
        testSubmitSearchNoQuery: function() {
            try {
                LANE.search.submitSearch();
                Y.Assert.fail('should cause exception');
            } catch (ex) {
                Y.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
		testSourceChangeEvent: function() {
			var theSearchSource = this.searchSource;
			Y.Global.on('lane:searchSourceChange', function(value) {
				Y.Assert.areEqual(value, theSearchSource.get('value'));
			});
			this.searchSource.set('selectedIndex',1);
			this.searchSource.simulate('change');
		},
		testGetSearchTerms: function() {
			Y.Assert.areEqual('', LANE.search.getSearchTerms());
		},
		testSetSearchTerms: function() {
			LANE.search.setSearchTerms('foo');
			Y.Assert.areEqual('foo', this.searchTermsInput.get('value'));
		}
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
    
    Y.Test.Runner.add(searchTestCase);
    Y.Test.Runner.run();
});
