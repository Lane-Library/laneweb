"use strict";

var lanePopupTestCase = new Y.Test.Case({
    name: 'Lane Popup Test Case',
//  testConsole: function() {
//  Y.all('a').item(0).simulate('click');
//  },
//  testStandard: function() {
//  Y.all('a').item(1).simulate('click');
//  },
//  testConsoleWithScrollbars: function() {
//  Y.all('a').item(2).simulate('click');
//  },
//  testFullscreen: function() {
//  Y.all('a').item(3).simulate('click');
//  },
    testLocal: function() {
        Y.one('a').simulate('click');
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(lanePopupTestCase);
Y.Test.Runner.masterSuite.name = "popup-test.js";
Y.Test.Runner.run();
