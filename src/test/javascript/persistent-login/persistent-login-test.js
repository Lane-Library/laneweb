"use strict";

Y.io = function(url, config) {
    persistentLoginTestCase.url = url;
    config.on.success.apply(this,[0, {
        responseText : '<input type="checkbox" id="is-persistent-login" /> <div id="shibboleth-links"><a href="shibbolethPath" id="Stanford">Stanford University</a><a id="SHC">shc</a><input type="checkbox" id="is-persistent-login"/></div>'
    }]);
};

Y.Cookie = {
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
        Y.lane.Lightbox.hide();
        Y.lane.Lightbox.setContent("");
        this.url = null;
        this.cookieArgs = null;
    },

    "test redirectcme click" : function() {
        var node = Y.one("#cmeredirect");
        var handle = node.on("click", function(e) {
            e.preventDefault();
        });
        node.simulate("click");
        handle.detach();
        Y.Assert.areSame("/plain/shibboleth-persistent-extension.html", this.url);
    },

    "test proxylogin click" : function() {
        var node = Y.one("#proxylogin");
        var handle = node.on("click", function(e) {
            e.preventDefault();
        });
        node.simulate("click");
        handle.detach();
        Y.Assert.areSame("/plain/shibboleth-persistent-extension.html", this.url);
    },

    "test stanford click" : function() {
        var node = Y.one("#proxylogin");
        var handle1 = node.on("click", function(e) {
            e.preventDefault();
        });
        node.simulate("click");
        handle1.detach();
        var stanford = Y.one("#Stanford");
        var handle2 = stanford.on("click", function(e) {
            e.preventDefault();
        });
        stanford.simulate("click");
        handle2.detach();
        var expected = stanford.get("pathname") + stanford.get("search");
        Y.Assert.areSame("/persistentLogin.html?pl=renew&url=https%253A%252F%252Flogin.laneproxy.stanford.edu%252Flogin%253Furl%253Dfoo", expected);
    },

    "test stanford click 2" : function() {
        var node = Y.one("#proxylogin2");
        var handle1 = node.on("click", function(e) {
            e.preventDefault();
        });
        node.simulate("click");
        handle1.detach();
        var stanford = Y.one("#Stanford");
        var handle2 = stanford.on("click", function(e) {
            e.preventDefault();
        });
        stanford.simulate("click");
        handle2.detach();
        var expected = stanford.get("pathname") + stanford.get("search");
        Y.Assert.areSame("/persistentLogin.html?pl=renew&url=http%253A%252F%252Flaneproxy.stanford.edu%252Flogin%253Furl%253Dfoo", expected);
    },

    "test unchecked is persistent login" : function() {
        var node = Y.one("#is-persistent-login");
        node.set("checked", false);
        node.simulate("change");
        Y.Assert.areSame("remove", this.cookieArgs.name);
        Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
    },

    "test checked is persistent login" : function() {
        var node = Y.one("#is-persistent-login");
        node.set("checked", true);
        node.simulate("change");
        Y.Assert.areSame("set", this.cookieArgs.name);
        Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
        Y.Assert.areSame("yes", this.cookieArgs.args[1]);
    },

    "test persistent-login click" : function() {
        var node = Y.one("#persistent-login");
        var href = null;
        var handle = Y.lane.Location.on("hrefChange", function(event) {
            event.preventDefault();
            href = event.newVal;
        });
        node.simulate("click");
        handle.detach();
        Y.Assert.areSame("/persistentLogin.html?pl=renew&url=/myaccounts.html", href);
    }

});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop : false
}).render('#log');

Y.Test.Runner.add(persistentLoginTestCase);
Y.Test.Runner.masterSuite.name = "persistent-login.js";
Y.Test.Runner.run();