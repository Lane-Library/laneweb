/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){

    var metasearchTestCase = new Y.Test.Case({
        name: 'Lane Metasearch Test Case'
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(metasearchTestCase);
    Y.Test.Runner.run();
});

//    var url;
//    var success;
//    var jsonResult;
//    YAHOO.util.Connect = {
//        asyncRequest: function(method, theUrl, object) {
//            url = theUrl;
//            success = object.success;
//        }
//    };
//    YAHOO.lang.JSON = {
//        parse: function() {
//            return jsonResult;
//        }
//    };
//        
//        testURL: function() {
//            LANE.search.metasearch.initialize();
//            LANE.search.metasearch.getResultCounts();
//            YAHOO.util.Assert.isTrue(url.indexOf('/././apps/search/json?q=foo&r=foo&rd=') === 0);
//        },
//        
//        testSuccess: function() {
//            LANE.search.metasearch.initialize();
//            LANE.search.metasearch.getResultCounts();
//            jsonResult = {
//                resources: []
//            };
//            success({});
//        }


