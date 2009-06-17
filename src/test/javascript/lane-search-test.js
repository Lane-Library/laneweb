/**
 * @author ceyates
 */
(function(){
    
    var LANESearchTestCase = new YAHOO.tool.TestCase({
        name: "LaneSearch TestCase",

        testActivateDeactivateButton: function(){
            var searchSubmit = document.getElementById('searchSubmit');
            YAHOO.util.Assert.areEqual('search_btn.gif', searchSubmit.src.match(/search_btn.gif/));
            YAHOO.util.UserAction.mouseover(searchSubmit);
            YAHOO.util.Assert.areEqual('search_btn_f2.gif', searchSubmit.src.match(/search_btn_f2.gif/));
            YAHOO.util.UserAction.mouseout(searchSubmit);
            YAHOO.util.Assert.areEqual('search_btn.gif', searchSubmit.src.match(/search_btn.gif/));
        },

        testStartSearch: function(){
            var searchInput = document.getElementById('searchTerms'),
                searchIndicator = document.getElementById('searchIndicator');
            YAHOO.util.Assert.areEqual('hidden', searchIndicator.style.visibility);
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            searchInput.value = 'hello';
            LANE.search.startSearch();
            YAHOO.util.Assert.isTrue(LANE.search.isSearching());
            YAHOO.util.Assert.areEqual('visible', searchIndicator.style.visibility);
            LANE.search.stopSearch();
            YAHOO.util.Assert.areEqual('hidden', searchIndicator.style.visibility);
            searchInput.value = '';
        },
        testStartSearchNoQuery: function(){
            try {
                LANE.search.startSearch();
            } catch (ex) {
                YAHOO.util.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
        testSetSearchSource: function() {
            var searchLabel = document.getElementById('searchLabel'),
                searchSource = document.getElementById('searchSource'),
                allSearchTab = document.getElementById('allSearchTab'),
                ejSearchTab = document.getElementById('ejSearchTab');
            YAHOO.util.Assert.areEqual('all', LANE.search.getSearchSource());
            YAHOO.util.Assert.areEqual('',ejSearchTab.className);
            YAHOO.util.Assert.areEqual('activeSearchTab', allSearchTab.className);
            LANE.search.setSearchSource('ej');
            YAHOO.util.Assert.areEqual('eJournals', searchLabel.innerHTML);
            YAHOO.util.Assert.areEqual('ej', LANE.search.getSearchSource());
            YAHOO.util.Assert.areEqual('',allSearchTab.className);
            YAHOO.util.Assert.areEqual('activeSearchTab', ejSearchTab.className);
        }
    });
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(LANESearchTestCase);
    YAHOO.util.Event.addListener(this,'load',function(){
        YAHOO.tool.TestRunner.run();
    });
})();
