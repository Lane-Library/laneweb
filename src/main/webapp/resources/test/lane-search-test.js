/**
 * @author ceyates
 */
if (!window.location.search) {
    window.location = window.location + '?source=foo&q=bar+baz';
}
YUI({ logInclude: { TestRunner: true } }).use('lane-result','node-event-simulate','console','test', function(Y) {
    
    var resultTestCase = new Y.Test.Case({
        
        name: 'Lane Result Test Case',
        result: LANE.search.Result,
        
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
    
    var searchTestCase = new Y.Test.Case({
		
		name: 'Lane Search Test Case',
        search: LANE.search.Search,
		
        searchTermsInput: Y.one('#searchTerms'),
        searchIndicator: Y.one('#searchIndicator'),
		searchSource: Y.one('#searchSource'),
        
        setUp: function() {
            this.searchTermsInput.set('value', '');
            this.searchTermsInput.set('title', '');
			this.searchSource.set('selectedIndex',0);
        },
    
        testStartStopSearch: function() {
            Y.Assert.areEqual('hidden', this.searchIndicator.getStyle('visibility'));
            this.searchTermsInput.set('value', 'hello');
            this.search.startSearch();
            Y.Assert.areEqual('visible', this.searchIndicator.getStyle('visibility'));
            this.search.stopSearch();
            Y.Assert.areEqual('hidden', this.searchIndicator.getStyle('visibility'));
        },
        testSubmitSearchNoQuery: function() {
            try {
                this.search.submitSearch();
                Y.Assert.fail('should cause exception');
            } catch (ex) {
                Y.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
		testSourceChangeEvent: function() {
			var theSearchSource = this.searchSource;
			Y.Global.on('lane:searchSourceChange', function(search) {
				Y.Assert.areEqual(search.getSearchSource(), theSearchSource.get('value'));
			});
			this.searchSource.set('selectedIndex',1);
			this.searchSource.simulate('change');
		},
		testGetSearchTerms: function() {
			Y.Assert.areEqual('', this.search.getSearchTerms());
		},
		testSetSearchTerms: function() {
			this.search.setSearchTerms('foo');
			Y.Assert.areEqual('foo', this.searchTermsInput.get('value'));
		}
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
    
    Y.Test.Runner.add(resultTestCase);
    Y.Test.Runner.add(searchTestCase);
    Y.Test.Runner.run();
});
