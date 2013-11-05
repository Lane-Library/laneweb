(function(){

    var purchaseSuggestionsTestCase = new Y.Test.Case({
        name: 'purchase-suggestions Test Case'
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(purchaseSuggestionsTestCase);
    Y.Test.Runner.masterSuite.name = "purchase-suggestions.js";
    Y.Test.Runner.run();
    
})();
