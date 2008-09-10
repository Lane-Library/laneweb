/**
 * @author ceyates
 */
(function() {
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
var UserAction = YAHOO.util.UserAction;

var LANEQuerymapTestCase = new TestCase({
    name:'querymap test',
    testQueryMap: function() {
        /*
var qm = new LANE.search.QueryMap();
        qm.addListener(function(evt){
            alert(evt.length + ' ' + evt);
        });
*/
    }
});
var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANEQuerymapTestCase);
TestRunner.run();
})();