(function(){

    Y.io = function(url, config) {
        config.on.success.apply(this, [0,{responseText:'<div><a id="yes-persistent-login">yes</a><a id="no-persistent-login">no</a><input type="checkbox" id="dont-ask-again"/></div>'}]);
    };
    var persistentLoginTestCase = new Y.Test.Case({
        name: 'persistent-login Test Case',
        
        tearDown: function() {
            Y.lane.Lightbox.hide();
            Y.lane.Lightbox.setContent("");
        },
        
        testLoginClickYesClick: function() {
            Y.one("#login").simulate("click");
            var yes = Y.one("#yes-persistent-login");
            var handle = yes.on("click", function(event) {
                event.preventDefault();
            });
            yes.simulate("click");
            handle.detach();
            //TODO: why &url=, not ?url=
            Y.Assert.isTrue(yes.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
        },
        
        testLoginClickNoClick: function() {
            Y.one("#login").simulate("click");
            var no = Y.one("#no-persistent-login");
            var handle = no.on("click", function(event) {
                event.preventDefault();
            });
            no.simulate("click");
            handle.detach();
            //TODO: why &url=, not ?url=
            Y.Assert.isTrue(no.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
        },
        
        testProxyLoginClick: function() {
            Y.one("#proxylogin").simulate("click");
            var yes = Y.one("#yes-persistent-login");
            var handle = yes.on("click", function(event) {
                event.preventDefault();
            });
            yes.simulate("click");
            handle.detach();
            //TODO: why &url=, not ?url=
            Y.Assert.isTrue(yes.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
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
