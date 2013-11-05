(function(){

    var menuDelayTestCase = new Y.Test.Case({
        name: 'menu-delay Test Case'
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(menuDelayTestCase);
    Y.Test.Runner.masterSuite.name = "menu-delay.js";
    Y.Test.Runner.run();
    
})();
