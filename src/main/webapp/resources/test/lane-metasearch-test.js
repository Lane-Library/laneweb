/**
 * @author ceyates
 */
(function() {

    var url;
    var success;
    var jsonResult;
    YAHOO.util.Connect = {
        asyncRequest: function(method, theUrl, object) {
            url = theUrl;
            success = object.success;
        }
    };
    YAHOO.lang.JSON = {
        parse: function() {
            return jsonResult;
        }
    };
    var metasearchTestCase = new YAHOO.tool.TestCase({
    
        name: "Lane Metasearch Test Case",
        
        testURL: function() {
            LANE.search.metasearch.initialize();
            LANE.search.metasearch.getResultCounts();
            YAHOO.util.Assert.isTrue(url.indexOf('/././apps/search/json?q=foo&r=foo&rd=') === 0);
        },
        
        testSuccess: function() {
            LANE.search.metasearch.initialize();
            LANE.search.metasearch.getResultCounts();
            jsonResult = {
                resources: []
            };
            success({});
        }
    });

    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(metasearchTestCase);
    YAHOO.util.Event.addListener(this, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();
