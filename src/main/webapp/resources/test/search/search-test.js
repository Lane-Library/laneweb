/**
 * @author ceyates
 */
YUI({ logInclude: { TestRunner: true } }).use('node-event-simulate','console','test', function(T) {

    
    var searchTestCase = new T.Test.Case({
        
        name: 'Lane Search Test Case',
        search: Y.lane.Search,
        
        searchTermsInput: T.one('#searchTerms'),
        searchIndicator: T.one('#searchIndicator'),
        searchSource: T.one('#searchSource'),
        
        setUp: function() {
            this.searchTermsInput.set('value', '');
            this.searchTermsInput.set('title', '');
            this.searchSource.set('selectedIndex',0);
        },
        testSubmitSearchNoQuery: function() {
            try {
                this.search.submitSearch();
                T.Assert.fail('should cause exception');
            } catch (ex) {
                T.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
        testSourceChangeEvent: function() {
            var theSearchSource = this.searchSource;
            T.Global.on('lane:searchSourceChange', function(search) {
                T.Assert.areEqual(search.getSearchSource(), theSearchSource.get('value'));
            });
            this.searchSource.set('selectedIndex',1);
            this.searchSource.simulate('change');
        },
        testGetSearchTerms: function() {
            T.Assert.areEqual('', this.search.getSearchTerms());
        },
        testSetSearchTerms: function() {
            this.search.setSearchTerms('foo');
            T.Assert.areEqual('foo', this.searchTermsInput.get('value'));
        },
        testSuggestSelect: function() {
            T.publish("lane:suggestSelect",{broadcast:2});
            T.fire("lane:suggestSelect");
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false                   
    }).render('#log');
    
    T.Test.Runner.add(searchTestCase);
    T.Test.Runner.run();
});
