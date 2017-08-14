"use strict";

var menuTestCase = new Y.Test.Case({

    name: 'Lane Menu Test Case',
    
    testCloseItem: function() {
        var up = Y.one(".fa-chevron-up");
        up.simulate("click");
        Y.Assert.isTrue(up._node.className === 'fa fa-chevron-down');
    }

});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(menuTestCase);
Y.Test.Runner.masterSuite.name = "menu-test.js";
Y.Test.Runner.run();
