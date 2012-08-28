/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(T) {

    var lanePopupTestCase = new T.Test.Case({
        name: 'Lane Popup Test Case',
//        testConsole: function() {
//            T.all('a').item(0).simulate('click');
//        },
//        testStandard: function() {
//            T.all('a').item(1).simulate('click');
//        },
//        testConsoleWithScrollbars: function() {
//            T.all('a').item(2).simulate('click');
//        },
//        testFullscreen: function() {
//            T.all('a').item(3).simulate('click');
//        },
        testLocal: function() {
            T.one('a').simulate('click');
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(lanePopupTestCase);
    T.Test.Runner.run();
});
