"use strict";

var purchaseSuggestionsTestCase = new Y.Test.Case({
    name: 'purchase-suggestions Test Case',

    testThatItRenders: function() {
        var feedback = Y.one("#feedback");
        feedback.remove();
        Y.lane.Lightbox.set("url", "url");
        Y.lane.Lightbox.setContent(feedback.get("outerHTML"));
        Y.lane.Lightbox.show();
    },

    testClick: function() {
        var menu1 = Y.all("#purchase li").item(1);
        menu1.simulate("click");
        var text = Y.one(".yui3-purchase-item-active").get("text");
        Y.Assert.areSame("item1div1", text.replace(/\s+/, "") );
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(purchaseSuggestionsTestCase);
Y.Test.Runner.masterSuite.name = "purchase-suggestions.js";
Y.Test.Runner.run();
