/**
 * @author ceyates
 */
//this really isn't a unit test, but relys on setting the gDomain variable
//in the webtrends file to a local server so you can observer the requests
//by tailing the log.
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('lane-tracking','node-event-simulate', 'console', 'test', function(Y){

    var webtrendsTestCase = new Y.Test.Case({
        name: "Lane Webtrends TestCase",
        testTrack: function() {
            var handler = function(e) {
                e.preventDefault();
            };
            var nodes = Y.all('img, a'), i;
            for (i = 0; i < nodes.size(); i++) {
                nodes.item(i).on('click', handler);
                nodes.item(i).simulate('click');
                nodes.item(i).detach(handler);
            }
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(webtrendsTestCase);
    Y.Test.Runner.run();
});