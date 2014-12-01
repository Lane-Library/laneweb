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
        config.on.success.apply(this, [0,{responseText:'<input type="checkbox" id="is-persistent-login" /> <div id="shibboleth-links"><a href="shibbolethPath" id="Stanford">Stanford University</a><a id="SHC">shc</a><input type="checkbox" id="is-persistent-login"/></div>'}]);
    };
    var persistentLoginTestCase = new Y.Test.Case({
        name: 'persistent-login Test Case',

        cookie: null,

        href: null,

        path: null,
        
        
        
        tearDown: function() {
            Y.lane.Lightbox.hide();
            Y.lane.Lightbox.setContent("");
            this.href = null;
            this.path = null;
        },

        testStanfordLoginClick: function() {
//            Y.one("#login").simulate("click");
//             var stanford = Y.one("#Stanford");
//                var handle = stanford.on("click", function(event) {
//                    event.preventDefault();
//                });
//                stanford.simulate("click");
//                handle.detach();
//                Y.Assert.isTrue(stanford.get("href").indexOf("/shibbolethPath%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse") > 0);
//                
        },

//
//            
//        testProxyLoginClick: function() {
//            Y.one("#proxylogin").simulate("click");
//             var stanford = Y.one("#Stanford");
//                var handle = stanford.on("click", function(event) {
//                    event.preventDefault();
//                });
//                stanford.simulate("click");
//                handle.detach();
//                Y.Assert.isTrue(stanford.get("href").indexOf("/shibbolethPath%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse") > 0);
//            
//        },
//        
//        testCmeRedirectClick: function() {
//            Y.one("#cmeredirect").simulate("click");
//                var stanford = Y.one("#Stanford");
//                var handle = stanford.on("click", function(event) {
//                    event.preventDefault();
//                });
//                stanford.simulate("click");
//                handle.detach();
//                Y.Assert.isTrue(stanford.get("href").indexOf("/shibbolethPath%2Fsecure%2FpersistentLogin.html%3Fpl%3Dfalse") > 0);
//        },
//        
//        testSetCookie: function() {
//            Y.one("#cmeredirect").simulate("click");
//                var stanford = Y.one("#Stanford");
//                Y.one("#is-persistent-login").set("checked",true);
//                var handle = stanford.on("click", function(event) {
//                	event.preventDefault();
//                });
//                stanford.simulate("click");
//                Y.Cookie.set("persistent-preference", "1000");
//                handle.detach();
//                Y.Assert.isTrue(stanford.get("href").indexOf("/shibbolethPath%2Fsecure%2FpersistentLogin.html%3Fpl%3Dtrue") > 0);
//        },	
        
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(persistentLoginTestCase);
    Y.Test.Runner.masterSuite.name = "persistent-login.js";
    Y.Test.Runner.run();
})();
