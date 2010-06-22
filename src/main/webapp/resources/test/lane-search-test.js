/**
 * @author ceyates
 */
if (!window.location.search) {
    window.location = window.location + '?source=foo&q=bar+baz';
}
YUI({ logInclude: { TestRunner: true } }).use('lane', "lane-search-indicator", 'lane-textinputs', 'lane-suggest', 'lane-search-result','node-event-simulate','console','test', function(Y) {
    
    var resultTestCase = new Y.Test.Case({
        
        name: 'Lane Result Test Case',
        result: Y.lane.SearchResult,
        
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
        search: LANE.Search,
        
        searchTermsInput: Y.one('#searchTerms'),
        searchIndicator: Y.one('#searchIndicator'),
        searchSource: Y.one('#searchSource'),
        
        setUp: function() {
            this.searchTermsInput.set('value', '');
            this.searchTermsInput.set('title', '');
            this.searchSource.set('selectedIndex',0);
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
        },
        testSuggestSelect: function() {
            Y.publish("lane:suggestSelect",{broadcast:2});
            Y.fire("lane:suggestSelect");
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
