/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y) {

    Y.io = function(url, config) {
        if (url.indexOf("banner=1") > 0) {
            config.on.success.apply(config.context, [0, {responseText:Y.JSON.stringify("<?xml version='1.0' encoding='UTF-8'?><html xmlns='http://www.w3.org/1999/xhtml'> <head> <title>banner1</title> </head> <body> <img src='../../images/plus.png' /> <div class='banner-content'>banner1</div> <ul class='banner-nav'> <li><a href='?banner=1' class='banner-nav-active'></a></li> <li><a href='?banner=2'></a></li> </ul> </body></html>")}, {}]);
        } else if (url.indexOf("banner=2") > 0) {
            config.on.success.apply(config.context, [0, {responseText:Y.JSON.stringify("<?xml version='1.0' encoding='UTF-8'?><html xmlns='http://www.w3.org/1999/xhtml'> <head> <title>banner1</title> </head> <body> <img src='../../images/plus.png' /> <div class='banner-content'>banner2</div> <ul class='banner-nav'> <li><a href='?banner=1'></a></li> <li><a href='?banner=2' class='banner-nav-active'></a></li> </ul> </body></html>")}, {}]);
        }
    };

    var bannerTestCase = new Y.Test.Case({
		name : 'Banner Test Case',

		banner: Y.Widget.getByNode("#banner"),

		testAnchorActive: function() {
		    var anchors = Y.all(".banner-nav a");
		    var bannerContent = Y.one(".banner-content");
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", bannerContent.get("text"));
            this.wait(function() {
                Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
                Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
                Y.Assert.areEqual("banner2", bannerContent.get("text"));
            }, 10000);
		},

		testNavAnchorClick: function() {
            var anchors = Y.all(".banner-nav a");
            var bannerContent = Y.one(".banner-content");
            Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner2", bannerContent.get("text"));
            anchors.item(0).simulate("click");
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", bannerContent.get("text"));
            Y.Assert.isFalse(this.banner.get("automate"));
		},

		testNavLeftArrow: function() {
		    var anchors = Y.all(".banner-nav a");
		    var bannerContent = Y.one(".banner-content");
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", bannerContent.get("text"));
		    Y.one("doc").simulate("keyup", { keyCode: 37 });
            Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner2", bannerContent.get("text"));
		},

		testNavRightArrow: function() {
            var anchors = Y.all(".banner-nav a");
            var bannerContent = Y.one(".banner-content");
            Y.Assert.isFalse(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isTrue(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner2", bannerContent.get("text"));
            Y.one("doc").simulate("keyup", { keyCode: 39 });
            Y.Assert.isTrue(anchors.item(0).hasClass("banner-nav-active"));
            Y.Assert.isFalse(anchors.item(1).hasClass("banner-nav-active"));
            Y.Assert.areEqual("banner1", bannerContent.get("text"));
		}

	});

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bannerTestCase);
    Y.Test.Runner.masterSuite.name = "banner-test.js";
    Y.Test.Runner.run();
});
