/**
 * @author ceyates
 */
(function() {
    var searchTestCase = new YAHOO.tool.TestCase({
    
        testStartSearch: function() {
            var searchTermsInput = document.getElementById('searchTerms');
            var searchIndicator = document.getElementById('searchIndicator');
            var initialValue = searchTermsInput.value;
            YAHOO.util.Assert.areEqual('hidden', searchIndicator.style.visibility);
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            searchTermsInput.value = 'hello';
            LANE.search.startSearch();
            YAHOO.util.Assert.isTrue(LANE.search.isSearching());
            YAHOO.util.Assert.areEqual('visible', searchIndicator.style.visibility);
            LANE.search.stopSearch();
            YAHOO.util.Assert.areEqual('hidden', searchIndicator.style.visibility);
            searchTermsInput.value = initialValue;
        },
        testSetInitialText: function() {
            var searchTermsInput = document.getElementById('searchTerms');
            LANE.search.setInitialText();
            YAHOO.util.Assert.areEqual('all tab title', searchTermsInput.value);
        },
        testIsSearching: function() {
            var searchTermsInput = document.getElementById('searchTerms');
            var initialValue = searchTermsInput.value;
            searchTermsInput.value = 'foo';
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            LANE.search.startSearch();
            YAHOO.util.Assert.isTrue(LANE.search.isSearching());
            LANE.search.stopSearch();
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            searchTermsInput.value = initialValue;
            
        },
        testStartSearchNoQuery: function() {
            try {
                LANE.search.startSearch();
                YAHOO.util.Assert.fail('should cause exception');
            } catch (ex) {
                YAHOO.util.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
        testResetQValue: function() {
            var searchTermsInput = document.getElementById('searchTerms');
            var initialValue = searchTermsInput.value;
            YAHOO.util.Assert.areNotEqual(0, searchTermsInput.value.length);
            searchTermsInput.focus();
            YAHOO.util.Assert.areEqual(0, searchTermsInput.value.length);
            searchTermsInput.value = initialValue;
        }
    });
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(searchTestCase);
    YAHOO.util.Event.addListener(window, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();
