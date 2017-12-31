"use strict";

L.io = function(url, config) {
    config.on.success.apply(this, [0,{responseText:'<div><a href="/foo?bar=baz" id="yes-bookmark-login">yes</a><a id="no-bookmark-login">no</a></div>'}, config.arguments]);
};

var bookmarkLoginTestCase = new Y.Test.Case({

    name : "BookmarkLogin Test Case",

    login : L.BookmarkLogin,

    testAddBookmark: function() {
        this.login.addBookmark("label", "url");
        var loc = encodeURIComponent(location.href);
        var yes = Y.one("#yes-bookmark-login");
        var no = Y.one("#no-bookmark-login");
        var doc = location.protocol + "//" + location.host;
        Y.Assert.areSame(doc + "/foo?bar=baz&label=label&url=url&redirect=" + loc, yes.get("href"));
        no.simulate("click");
        Y.Assert.areSame(false, L.Lightbox.get("visible"));
    }

});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(bookmarkLoginTestCase);
Y.Test.Runner.masterSuite.name = "bookmark-login-test.js";
Y.Test.Runner.run();
