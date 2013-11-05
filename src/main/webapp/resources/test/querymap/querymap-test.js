(function(){

    var querymapTestCase = new Y.Test.Case({
        name: 'querymap Test Case'
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(querymapTestCase);
    Y.Test.Runner.masterSuite.name = "querymap.js";
    Y.Test.Runner.run();
    
})();
