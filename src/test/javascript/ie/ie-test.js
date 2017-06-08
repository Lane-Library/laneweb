"use strict";

var ieTestCase = new Y.Test.Case({
    name: 'Lane IE Test Case',

    testBookmarkletNotIEDisplayNone: function() {
        Y.Assert.areEqual("none", document.querySelector("#bookmarkletNotIE").style.display);
    },

    testBookmarkletIEDisplayBlock: function() {
        Y.Assert.areEqual("block", document.querySelector("#bookmarkletIE").style.display);
    }
});

document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(ieTestCase);
Y.Test.Runner.masterSuite.name = "ie-test.js";
Y.Test.Runner.run();
