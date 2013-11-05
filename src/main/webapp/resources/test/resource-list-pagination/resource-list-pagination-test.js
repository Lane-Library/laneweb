(function(){

    var resourceListPaginationTestCase = new Y.Test.Case({
        name: 'resource-list-pagination Test Case'
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(resourceListPaginationTestCase);
    Y.Test.Runner.masterSuite.name = "resource-list-pagination.js";
    Y.Test.Runner.run();
    
})();
