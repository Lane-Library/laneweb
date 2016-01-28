"use strict";

var menuDelayTestCase = new Y.Test.Case({
    name: 'menu-delay Test Case',

    testMouseOver: function() {
        var menu = Y.one("ul ul");
        menu.simulate("mouseover");
        Y.Assert.areSame("hidden", menu.getStyle("visibility"));
        this.wait(function() {
            Y.Assert.areSame("visible", menu.getStyle("visibility"));
        }, 600);
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(menuDelayTestCase);
Y.Test.Runner.masterSuite.name = "menu-delay.js";
Y.Test.Runner.run();
