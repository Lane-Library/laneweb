YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    document.addEventListener("click", function(event) {
        event.preventDefault();
    });

    window.open = function() {
        return {
            focus: function() {},
            close: function() {}
        }
    };

    let lanePopupTestCase = new Y.Test.Case({

        name: 'Lane Popup Test Case',

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

    new Y.Test.Console().render();

    Y.Test.Runner.add(lanePopupTestCase);
    Y.Test.Runner.masterSuite.name = "popup-test.js";
    Y.Test.Runner.run();

});