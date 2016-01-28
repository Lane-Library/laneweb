"use strict";

Y.io = function(url, config) {
    if (url.indexOf("banner=1") > 0) {
        config.on.success.apply(config.context, [0, {responseText:Y.JSON.stringify("<?xml version='1.0' encoding='UTF-8'?><html xmlns='http://www.w3.org/1999/xhtml'> <head> <title>banner1</title> </head> <body> <div> <div class='banner-content'>banner1</div></div> <ul class='banner-nav'> <li><a href='?banner=1' class='banner-nav-active'></a></li> <li><a href='?banner=2'></a></li> </ul> </body></html>")}, {}]);
    } else if (url.indexOf("banner=2") > 0) {
        config.on.success.apply(config.context, [0, {responseText:Y.JSON.stringify("<?xml version='1.0' encoding='UTF-8'?><html xmlns='http://www.w3.org/1999/xhtml'> <head> <title>banner1</title> </head> <body> <div> <div class='banner-content'>banner2</div></div> <ul class='banner-nav'> <li><a href='?banner=1'></a></li> <li><a href='?banner=2' class='banner-nav-active'></a></li> </ul> </body></html>")}, {}]);
    }
};

var bannerTestCase = new Y.Test.Case({

    name : 'Banner Test Case',

    banner : Y.Widget.getByNode("#banner"),

    testAnchorActive : function() {
        var anchors = Y.all(".banner-nav a");
        Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
        Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
        Y.Assert.areEqual("banner1", Y.one(".banner-content").get("text"));
        this.wait(function() {
            Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner2", Y.one(".banner-content").get("text"));
        }, 12000);
    },

    testNavAnchorClick : function() {
        var anchors = Y.all(".banner-nav a");
        Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
        Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
        Y.Assert.areEqual("banner2", Y.one(".banner-content").get("text"));
        anchors.item(0).simulate("click");
        this.wait(function() {
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", Y.one(".banner-content").get("text"));
            Y.Assert.isFalse(this.banner.get("automate"));
        }, 1500);
    },

    testNavLeftArrow : function() {
        var anchors = Y.all(".banner-nav a");
        Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
        Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
        Y.Assert.areEqual("banner1", Y.one(".banner-content").get("text"));
        Y.one("doc").simulate("keyup", {
            keyCode : 37
        });
        this.wait(function() {
            Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner2", Y.one(".banner-content").get("text"));
        }, 1500);
    },

    testNavRightArrow : function() {
        var anchors = Y.all(".banner-nav a");
        Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
        Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
        Y.Assert.areEqual("banner2", Y.one(".banner-content").get("text"));
        Y.one("doc").simulate("keyup", {
            keyCode : 39
        });
        this.wait(function() {
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", Y.one(".banner-content").get("text"));
        }, 1500);
    }

});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop : false
}).render('#log');

Y.Test.Runner.add(bannerTestCase);
Y.Test.Runner.masterSuite.name = "banner-test.js";
Y.Test.Runner.run();
