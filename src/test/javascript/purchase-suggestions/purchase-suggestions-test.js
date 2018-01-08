"use strict";

var purchaseSuggestionsTestCase = new Y.Test.Case({
    name: 'purchase-suggestions Test Case',

    testThatItRenders: function() {
        var feedback = Y.one("#feedback");
        feedback.remove();
        L.Lightbox.set("url", "url");
        L.Lightbox.setContent(feedback.get("outerHTML"));
        L.Lightbox.show();
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
