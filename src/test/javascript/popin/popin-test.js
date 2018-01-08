"use strict";

var popinTestCase = new Y.Test.Case({
    name: 'Lane Popin Test Case',

    testPopinEvent: function() {
        var node = Y.one("#spellCheck");
        node.setStyle("display", "inline");
        L.fire("lane:popin", "spellCheck");
        Y.Assert.isTrue(node.hasClass("popin-active"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(popinTestCase);
Y.Test.Runner.masterSuite.name = "popin-test.js";
Y.Test.Runner.run();
