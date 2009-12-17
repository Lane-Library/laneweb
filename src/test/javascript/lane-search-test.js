/**
 * @author ceyates
 */
(function(){
    var LANESearchTestCase = new YAHOO.tool.TestCase({
        name: "LaneSearch TestCase",
/*
        testStartSearch: function(){
            var d = document, i, q;
            q = d.getElementById('p').getElementsByTagName('input')[0];
            i = d.getElementById('searchIndicator');
            YAHOO.util.Assert.areEqual('hidden', i.style.visibility);
            YAHOO.util.Assert.isFalse(s.isSearching());
            q.value = 'hello';
            LANE.search.startSearch();
            YAHOO.util.Assert.isTrue(s.isSearching());
            YAHOO.util.Assert.areEqual('visible', i.style.visibility);
            LANE.search.stopSearch();
            YAHOO.util.Assert.areEqual('hidden', i.style.visibility);
            q.value = '';
        },
*/
        testIsSearching: function(){
            var q = document.getElementsByTagName('input')[0];
            var initialValue = q.value;
            q.value = 'foo';
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            LANE.search.startSearch();
            YAHOO.util.Assert.isTrue(LANE.search.isSearching());
            LANE.search.stopSearch();
            YAHOO.util.Assert.isFalse(LANE.search.isSearching());
            q.value = initialValue;
            
        },
        testStartSearchNoQuery: function(){
            try {
                LANE.search.startSearch();
                YAHOO.util.Assert.fail('should cause exception');
            } catch (ex) {
                YAHOO.util.Assert.areEqual('nothing to search for', ex.toString());
            }
        },
        testResetQValue: function() {
            var q = document.getElementsByTagName('input')[0];
            var initialValue = q.value;
            YAHOO.util.Assert.areNotEqual(0,q.value.length);
            q.focus();
            YAHOO.util.Assert.areEqual(0, q.value.length);
            q.value = initialValue;
        }
    });
    var oLogger = new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(LANESearchTestCase);
    YAHOO.tool.TestRunner.run();
})();
