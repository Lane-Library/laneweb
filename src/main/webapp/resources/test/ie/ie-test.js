"use strict";

var ieTestCase = new Y.Test.Case({
    name: 'Lane IE Test Case',

    testBookmarkletNotIEDisplayNone: function() {
        Y.Assert.areEqual("none", Y.one("#bookmarkletNotIE").getStyle("display"));
    },

    testBookmarkletIEDisplayBlock: function() {
        Y.Assert.areEqual("block", Y.one("#bookmarkletIE").getStyle("display"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(ieTestCase);
Y.Test.Runner.masterSuite.name = "ie-test.js";
Y.Test.Runner.run();
