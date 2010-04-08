/**
 * @author ceyates
 */
(function() {

    var LANEQuerymapTestCase = YAHOO.tool.TestCase({
        name: 'querymap test',
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
    YAHOO.tool.TestRunner.add(LANEQuerymapTestCase);
    YAHOO.tool.TestRunner.run();
})();
