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

    let persistentLoginTestCase = new Y.Test.Case({

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

        "test unchecked is persistent login" : function() {
            let node = document.querySelector("#is-persistent-login");
            node.checked = false;
            let event = document.createEvent("Event");
            event.initEvent("change", false, true); 
            node.dispatchEvent(event);
            Y.Assert.areSame("remove", this.cookieArgs.name);
            Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
        },

        "test checked is persistent login" : function() {
            let node = document.querySelector("#is-persistent-login");
            node.checked = true;
            let event = document.createEvent("Event");
            event.initEvent("change", false, true); 
            node.dispatchEvent(event);
            Y.Assert.areSame("set", this.cookieArgs.name);
            Y.Assert.areSame("isPersistent", this.cookieArgs.args[0]);
            Y.Assert.areSame("yes", this.cookieArgs.args[1]);
        },

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(persistentLoginTestCase);
    Y.Test.Runner.masterSuite.name = "persistent-login.js";
    Y.Test.Runner.run();

});
