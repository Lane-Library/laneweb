(function(){

    Y.lane.Location.on("hrefChange", function(event) {
        event.preventDefault();
        persistentLoginTestCase.href = event.newVal;
    });

    Y.all("a").on("click", function(event) {
        event.preventDefault();
        persistentLoginTestCase.path = event.target.get("pathname");
    });

    Y.io = function(url, config) {
        config.on.success.apply(this, [0,{responseText:'<div><a id="yes-persistent-login">yes</a><a id="no-persistent-login">no</a><input type="checkbox" id="dont-ask-again"/></div>'}]);
    };
    var persistentLoginTestCase = new Y.Test.Case({
        name: 'persistent-login Test Case',

        cookie: Y.Cookie.get("persistent-preference"),

        href: null,

        path: null,

        tearDown: function() {
            Y.lane.Lightbox.hide();
            Y.lane.Lightbox.setContent("");
            this.href = null;
            this.path = null;
//        },
//
//        testLoginClickYesClick: function() {
//            Y.one("#login").simulate("click");
//            if (this.cookie === "denied") {
//                Y.Assert.areSame("/secure/persistentLogin.html?pl=false&ur", this.href.substring(0, 40));
//            } else {
//                var yes = Y.one("#yes-persistent-login");
//                var handle = yes.on("click", function(event) {
//                    event.preventDefault();
//                });
//                yes.simulate("click");
//                handle.detach();
//                //TODO: why &url=, not ?url=
//                Y.Assert.isTrue(yes.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
//            }
//        },
//
//        testLoginClickNoClick: function() {
//            Y.one("#login").simulate("click");
//            if (this.cookie === "denied") {
//                Y.Assert.areSame("/secure/persistentLogin.html?pl=false&ur", this.href.substring(0, 40));
//            } else {
//                var no = Y.one("#no-persistent-login");
//                var handle = no.on("click", function(event) {
//                    event.preventDefault();
//                });
//                no.simulate("click");
//                handle.detach();
//                //TODO: why &url=, not ?url=
//                Y.Assert.isTrue(no.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
//            }
//        },
//
//        testProxyLoginClick: function() {
//            Y.one("#proxylogin").simulate("click");
//            if (this.cookie === "denied") {
//                Y.Assert.isTrue(-1 < this.path.indexOf("secure/apps/proxy/credential") < 2);
//            } else {
//                var yes = Y.one("#yes-persistent-login");
//                var handle = yes.on("click", function(event) {
//                    event.preventDefault();
//                });
//                yes.simulate("click");
//                handle.detach();
//                //TODO: why &url=, not ?url=
//                Y.Assert.isTrue(yes.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
//            }
//        },
//        
//        testCmeRedirectClick: function() {
//            Y.one("#cmeredirect").simulate("click");
//            if (this.cookie === "denied") {
//                Y.Assert.isTrue(-1 < this.path.indexOf("redirect/cme") < 2);
//            } else {
//                var yes = Y.one("#yes-persistent-login");
//                var handle = yes.on("click", function(event) {
//                    event.preventDefault();
//                });
//                yes.simulate("click");
//                handle.detach();
//                //TODO: why &url=, not ?url=
//                Y.Assert.isTrue(yes.get("href").indexOf("/secure/persistentLogin.html&url=") > 0);
//            }
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
