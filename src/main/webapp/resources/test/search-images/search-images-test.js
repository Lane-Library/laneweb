"use strict";

var searchImagesTestCase = new Y.Test.Case({
    name: 'lane-search-images Test Case',
    
    reset: {},
    
    setUp: function() {
        this.formHtml = Y.one("form").get("innerHTML");
    },
    
    tearDown: function() {
        Y.one("form").set("innerHTML", this.formHtml);
    },
    
    testSubmitPageination: function() {
        var submitted = false;
        Y.one("form").once("submit", function(event) {
            submitted = true;
            event.preventDefault();
        });
        Y.one("input[name=page]").set("value", "3");
        Y.one("form").simulate("submit");
        Y.Assert.isTrue(submitted);
        Y.Assert.isNull(Y.one("input[name=totalPages]"));
    },
    
    testSubmitPageLessThanOne: function() {
        var alert = window.alert;
        var alertText = "";
        window.alert = function(text) {
            alertText = text;
        }
        Y.one("input[name=page]").set("value", "0");
        Y.one("form").simulate("submit");
        Y.Assert.areEqual("Page out of range", alertText);
        window.alert = alert;
        Y.Assert.isNotNull(Y.one("input[name=totalPages]"));
    },
    
    testSubmitPageGreaterThanTotal: function() {
        var alert = window.alert;
        var alertText = "";
        window.alert = function(text) {
            alertText = text;
        }
        Y.one("input[name=page]").set("value", "10");
        Y.one("form").simulate("submit");
        Y.Assert.areEqual("Page out of range", alertText);
        window.alert = alert;
        Y.Assert.isNotNull(Y.one("input[name=totalPages]"));
    },
    
    testImageListItemClick: function() {
        var item = Y.one("#li0");
        var detail = Y.one("#imageDetail_0");
        item.simulate("click");
        Y.Assert.areEqual("src", detail.one(".image").getAttribute("src"));
        Y.Assert.areEqual("shortTitle", detail.one("h3").get("text"));
        Y.Assert.areEqual("shortDescription", detail.one(".desc p").get("text"));
        Y.Assert.areEqual("block", detail.one(".article-title").getStyle("display"));
        Y.Assert.areEqual("shortArticleTitle", detail.one(".article-title p").get("text"));
        Y.Assert.areEqual("shortCopyrightText", detail.one(".copyright p").get("text"));
        Y.Assert.areEqual("pageUrl", detail.one(".to-image a").getAttribute("href"));
        Y.Assert.areEqual("imagedeco", item.one("div").get("className"));
        Y.Assert.areEqual("imageDetail", detail.get("className"));
    },
    
    testImageListRow2ItemClick: function() {
        var item = Y.one("#li2");
        var detail = Y.one("#imageDetail_2");
        item.simulate("click");
        Y.Assert.areEqual("src", detail.one(".image").getAttribute("src"));
        Y.Assert.areEqual("shortTitle", detail.one("h3").get("text"));
        Y.Assert.areEqual("shortDescription", detail.one(".desc p").get("text"));
        Y.Assert.areEqual("block", detail.one(".article-title").getStyle("display"));
        Y.Assert.areEqual("shortArticleTitle", detail.one(".article-title p").get("text"));
        Y.Assert.areEqual("shortCopyrightText", detail.one(".copyright p").get("text"));
        Y.Assert.areEqual("pageUrl", detail.one(".to-image a").getAttribute("href"));
        Y.Assert.areEqual("imagedeco", item.one("div").get("className"));
        Y.Assert.areEqual("imageDetail", detail.get("className"));
        Y.Assert.areEqual("#li1", location.hash);
    },
    
    testDetailCloseClick: function() {
        Y.one("#image-detail-close").simulate("click");
        Y.Assert.areEqual(0, Y.all(".imagedeco").size());
        Y.Assert.areEqual(0, Y.all(".imageDetail").size());
    },
    
    testAdminClick: function() {
        Y.one(".imagedeco-admin").simulate("click");
        Y.Assert.areEqual("admin-disable", Y.one("#li0").get("className"));
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(searchImagesTestCase);
Y.Test.Runner.masterSuite.name = "lane-search-images-test.js";
Y.Test.Runner.run();
