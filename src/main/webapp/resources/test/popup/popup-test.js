/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y) {

    var lanePopupTestCase = new Y.Test.Case({
        name: 'Lane Popup Test Case',
        testContainerExists: function() {
            Y.Assert.isTrue(Y.Lang.isObject(Y.one('#popupContainer')));
        },
        testConsole: function() {
            Y.all('a').item(0).simulate('click');
        },
        testStandard: function() {
            Y.all('a').item(1).simulate('click');
        },
        testConsoleWithScrollbars: function() {
            Y.all('a').item(2).simulate('click');
        },
        testFullscreen: function() {
            Y.all('a').item(3).simulate('click');
        },
        testLocal: function() {
            Y.all('a').item(4).simulate('click');
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    var yconsole = new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(lanePopupTestCase);
    Y.Test.Runner.run();
});
