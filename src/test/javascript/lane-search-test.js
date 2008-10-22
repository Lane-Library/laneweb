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
            var d = document, s;
            s = d.getElementById('searchSubmit');
            Assert.areEqual('search_btn.gif', s.src.match(/search_btn.gif/));
            YAHOO.util.UserAction.mouseover(s);
            Assert.areEqual('search_btn_f2.gif', s.src.match(/search_btn_f2.gif/));
            YAHOO.util.UserAction.mouseout(s);
            Assert.areEqual('search_btn.gif', s.src.match(/search_btn.gif/));
        },
        testStartSearch: function(){
            var d = document, s = LANE.search, i, q;
            q = d.getElementById('p').getElementsByTagName('input')[0];
            i = d.getElementById('searchIndicator');
            Assert.areEqual('hidden', i.style.visibility);
            Assert.isFalse(s.isSearching());
            q.value = 'hello';
            s.startSearch();
            Assert.isTrue(s.isSearching());
            Assert.areEqual('visible', i.style.visibility);
            s.stopSearch();
            Assert.areEqual('hidden', i.style.visibility);
            q.value = '';
        },
        testStartSearchNoQuery: function(){
            var d = document, s = LANE.search;
            try {
                s.startSearch();
            } catch (ex) {
                Assert.areEqual('nothing to search for', ex.toString());
            }
        }
    });
    var oLogger = new YAHOO.tool.TestLogger();
    TestRunner.add(LANESearchTestCase);
    TestRunner.run();
})();
