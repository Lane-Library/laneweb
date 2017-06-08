"use strict";

var bookmarkInstructionsTestCase = new Y.Test.Case({
    name : 'bookmark-instructions Test Case',
        
    test: function() {
        Y.Assert.isFalse(document.querySelector(".bookmark-instructions div").className === "expanded")
    }
});

document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop : false
}).render('#log');

Y.Test.Runner.add(bookmarkInstructionsTestCase);
Y.Test.Runner.masterSuite.name = "bookmark-instructions.js";
Y.Test.Runner.run();

alert(navigator.appName);
