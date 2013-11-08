(function(){

    var purchaseSuggestionsTestCase = new Y.Test.Case({
        name: 'purchase-suggestions Test Case',
        
        testThatItRenders: function() {
            var purchase = Y.one("#purchase");
            purchase.remove();
            Y.lane.Lightbox.setContent(purchase.get("outerHTML"));
            Y.lane.Lightbox.show();
        },
        
        testClick: function() {
            var menu1 = Y.all("li").item(1);
            menu1.simulate("click");
            Y.Assert.areSame("item1form1", Y.one(".yui3-purchase-item-active").get("text"));
        }
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(purchaseSuggestionsTestCase);
    Y.Test.Runner.masterSuite.name = "purchase-suggestions.js";
    Y.Test.Runner.run();
    
})();
