/**
 * @author ceyates
 */
(function(){
    var Assert = YAHOO.util.Assert;
    var TestRunner = YAHOO.tool.TestRunner;
    var TestCase = YAHOO.tool.TestCase;
    var UserAction = YAHOO.util.UserAction;
    var LANESearchTestCase = new TestCase({
        name: "LaneSearch TestCase",

        testActivateDeactivateButton: function(){
            var searchSubmit = document.getElementById('searchSubmit');
            Assert.areEqual('search_btn.gif', searchSubmit.src.match(/search_btn.gif/));
            YAHOO.util.UserAction.mouseover(searchSubmit);
            Assert.areEqual('search_btn_f2.gif', searchSubmit.src.match(/search_btn_f2.gif/));
            YAHOO.util.UserAction.mouseout(searchSubmit);
            Assert.areEqual('search_btn.gif', searchSubmit.src.match(/search_btn.gif/));
        },

        testStartSearch: function(){
            var searchInput = document.getElementById('searchTerms'),
                searchIndicator = document.getElementById('searchIndicator');
            Assert.areEqual('hidden', searchIndicator.style.visibility);
            Assert.isFalse(LANE.search.isSearching());
            searchInput.value = 'hello';
            LANE.search.startSearch();
            Assert.isTrue(LANE.search.isSearching());
            Assert.areEqual('visible', searchIndicator.style.visibility);
            LANE.search.stopSearch();
            Assert.areEqual('hidden', searchIndicator.style.visibility);
            searchInput.value = '';
        },
        testStartSearchNoQuery: function(){
            try {
                LANE.search.startSearch();
            } catch (ex) {
                Assert.areEqual('nothing to search for', ex.toString());
            }
        },
		testSetSearchSource: function() {
			var searchLabel = document.getElementById('searchLabel'),
			    searchSource = document.getElementById('searchSource'),
                allSearchTab = document.getElementById('allSearchTab'),
				ejSearchTab = document.getElementById('ejSearchTab');
		    Assert.areEqual('all', LANE.search.getSearchSource());
			Assert.areEqual('',ejSearchTab.className);
            Assert.areEqual('activeSearchTab', allSearchTab.className);
			LANE.search.setSearchSource('ej');
			Assert.areEqual('eJournals', searchLabel.innerHTML);
			Assert.areEqual('ej', LANE.search.getSearchSource());
            Assert.areEqual('',allSearchTab.className);
            Assert.areEqual('activeSearchTab', ejSearchTab.className);
		}
    });
    new YAHOO.tool.TestLogger();
    TestRunner.add(LANESearchTestCase);
	YAHOO.util.Event.addListener(this,'load',function(){
        TestRunner.run();
	});
})();
