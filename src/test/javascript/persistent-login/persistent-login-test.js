YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    L.io = function(url, config) {
        persistentLoginTestCase.url = url;
        config.on.success.apply(this,[0, {
            responseText : '<input type="checkbox" id="is-persistent-login" /> <div id="shibboleth-links"><a href="shibbolethPath" id="Stanford">Stanford University</a><a id="SHC">shc</a><input type="checkbox" id="is-persistent-login"/></div>'
        }]);
    };

    L.Cookie = {
            get : function() {
                persistentLoginTestCase.cookieArgs = {name:"get",args:arguments};
            },
            remove : function() {
                persistentLoginTestCase.cookieArgs = {name:"remove",args:arguments};
            },
            set : function() {
                persistentLoginTestCase.cookieArgs = {name:"set",args:arguments};
            }
    };

    var persistentLoginTestCase = new Y.Test.Case({

        name : 'persistent-login Test Case',

        url : null,

        cookieArgs : null,

        tearDown : function() {
            L.Lightbox.hide();
            L.Lightbox.setContent("");
            this.url = null;
            this.cookieArgs = null;
        },

        preventDefault: function(event) {
            event.preventDefault();
        },

        "test proxylogin click" : function() {
            var node = document.querySelector("#proxylogin");
            node.addEventListener("click", this.preventDefault);
            node.click();
            node.removeEventListener("click", this.preventDefault);
            Y.Assert.areSame("/plain/shibboleth-persistent-extension.html", this.url);
        },

        "test stanford click" : function() {
            var node = document.querySelector("#proxylogin");
            node.addEventListener("click", this.preventDefault);
            node.click();
            node.removeEventListener("click", this.preventDefault);
            var stanford = document.querySelector("#Stanford");
            stanford.addEventListener("click", this.preventDefault);
            stanford.click();
            stanford.removeEventListener("click", this.preventDefault);
            var expected = stanford.pathname + stanford.search;
            Y.Assert.areSame("/persistentLogin.html?pl=renew&url=https%3A%2F%2Flogin.laneproxy.stanford.edu%2Flogin%3Furl%3Dfoo", expected);
        },

        "test stanford click 2" : function() {
            var node = document.querySelector("#proxylogin2");
            node.addEventListener("click", this.preventDefault);
            node.click();
            node.removeEventListener("click", this.preventDefault);
            var stanford = document.querySelector("#Stanford");
            stanford.addEventListener("click", this.preventDefault);
            stanford.click();
            stanford.removeEventListener("click", this.preventDefault);
            var expected = stanford.pathname + stanford.search;
            Y.Assert.areSame("/persistentLogin.html?pl=renew&url=http%3A%2F%2Flaneproxy.stanford.edu%2Flogin%3Furl%3Dfoo", expected);
        },

        "test unchecked is persistent login" : function() {
            var node = document.querySelector("#is-persistent-login");
            node.checked = false;
            var event = document.createEvent("Event");
            event.initEvent("change", false, true); 
            node.dispatchEvent(event);
            Y.Assert.areSame("remove", this.cookieArgs.name);
            Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
        },

        "test checked is persistent login" : function() {
            var node = document.querySelector("#is-persistent-login");
            node.checked = true;
            var event = document.createEvent("Event");
            event.initEvent("change", false, true); 
            node.dispatchEvent(event);
            Y.Assert.areSame("set", this.cookieArgs.name);
            Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
            Y.Assert.areSame("yes", this.cookieArgs.args[1]);
        },

        "test persistent-login click" : function() {
            var node = document.querySelector("#persistent-login");
            var href = null;
            L.setLocationHref = function(h) {
                href = h;
            };
            node.click();
            Y.Assert.areSame("/persistentLogin.html?pl=renew&url=/index.html", href);
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(persistentLoginTestCase);
    Y.Test.Runner.masterSuite.name = "persistent-login.js";
    Y.Test.Runner.run();

});
