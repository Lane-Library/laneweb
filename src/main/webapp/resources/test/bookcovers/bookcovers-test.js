"use strict";

var bookcoversTestCase = new Y.Test.Case({

    name: "Lane Bookcovers Testcase",

    "test viewport:init": function() {
        var inview = 0;
        Y.lane.fire("viewport:init", {
            viewport: {
            inView: function() {
                return inview++ === 0;
            }
            }
        });
        Y.Assert.areEqual("http://www.example.com/src1", document.querySelector("img[data-bibid='1']").src)
    },

    "test viewport:scrolled": function() {
        Y.lane.fire("viewport:scrolled", {
            viewport: {
            inView: function() {
                return true;
            }
            }
        });
        Y.Assert.areEqual("http://www.example.com/src2", document.querySelector("img[data-bibid='2']").src)
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(bookcoversTestCase);
Y.Test.Runner.masterSuite.name = "bookcovers-test.js";
Y.Test.Runner.run();
