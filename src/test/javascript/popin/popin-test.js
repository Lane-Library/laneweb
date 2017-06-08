"use strict";

var popinTestCase = new Y.Test.Case({
    name: 'Lane Popin Test Case',

    testPopinEvent: function() {
        var node = Y.one("#spellCheck");
        node.setStyle("display", "inline");
        Y.fire("lane:popin", node);
        Y.Assert.isTrue(node.hasClass("active"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(popinTestCase);
Y.Test.Runner.masterSuite.name = "popin-test.js";
Y.Test.Runner.run();
