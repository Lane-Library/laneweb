"use strict";

L.io = function(url, config) {
    config.on.success.apply(this, [0, {responseText:"responseText"}]);
};

var lightboxTestCase = new Y.Test.Case({
    name: 'Lane Feedback Test Case',

    testAutoLightbox: function() {
        Y.Assert.areEqual("visible", Y.one(".yui3-lightbox").getStyle("visibility"));
    },

    testLightbox: function() {
        Y.one("a[rel=lightbox]").simulate("click");
        Y.Assert.areEqual("responseText", Y.one(".yui3-lightbox").get("text"));
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(lightboxTestCase);
Y.Test.Runner.masterSuite.name = "lightbox-test.js";
Y.Test.Runner.run();
