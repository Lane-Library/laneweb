"use strict";

var sameHeightTestCase = new Y.Test.Case({
    name: 'same-height Test Case',

    testSameHeight: function() {
        var divs1 = Y.all(".same-height-1");
        var div1 = Y.one(".same-height-1");
        var divs2 = Y.all(".same-height-2");
        var div2 = Y.one(".same-height-2");
        Y.Assert.areNotEqual(div1.get("clientHeight"), div2.get("clientHeight"));
        for (var i = 0; i < divs1.size(); i++) {
            Y.Assert.areEqual(div1.get("clientHeight"), divs1.item(i).get("clientHeight"));
        }
        for (var i = 0; i < divs2.size(); i++) {
            Y.Assert.areEqual(div2.get("clientHeight"), divs2.item(i).get("clientHeight"));
        }

    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(sameHeightTestCase);
Y.Test.Runner.masterSuite.name = "same-height.js";
Y.Test.Runner.run();
