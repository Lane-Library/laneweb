(function(){

    var resourceListPaginationTestCase = new Y.Test.Case({
        name: 'resource-list-pagination Test Case',
        
        testClick: function() {
            var button = Y.one(".pagingButton");
            button.simulate("click");
            Y.Assert.isTrue(button.hasClass("pagingButtonActive"));
            Y.Assert.isTrue(button.next(".pagingLabels").hasClass("show"));
        },
        
        testAnotherClick: function() {
            var button = Y.one(".pagingButton");
            button.simulate("click");
            Y.Assert.isFalse(button.hasClass("pagingButtonActive"));
            Y.Assert.isFalse(button.next(".pagingLabels").hasClass("show"));
        }
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(resourceListPaginationTestCase);
    Y.Test.Runner.masterSuite.name = "resource-list-pagination.js";
    Y.Test.Runner.run();
    
})();
