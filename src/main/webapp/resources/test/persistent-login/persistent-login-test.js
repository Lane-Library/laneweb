(function(){

    Y.io = function(url, config) {
        config.on.success.apply(this, [0,{responseText:'<div><a id="yes-persistent-login">yes</a><a id="no-persistent-login">no</a><input type="checkbox" id="dont-ask-again"/></div>'}]);
    };
    var persistentLoginTestCase = new Y.Test.Case({
        name: 'persistent-login Test Case',
        
        testLoginClick: function() {
            Y.one("#login").simulate("click");
            Y.Assert.areEqual("yes", Y.one("#yes-persistent-login").get("text"));
        }
    });

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(persistentLoginTestCase);
    Y.Test.Runner.masterSuite.name = "persistent-login.js";
    Y.Test.Runner.run();
    
})();
