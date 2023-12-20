YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    let lightboxTestCase = new Y.Test.Case({

        name: 'Lane Feedback Test Case',

        lightbox: L.Lightbox,

        testAutoLightbox: function() {
            Y.Assert.isTrue(this.lightbox.get("visible"));
            Y.Assert.areEqual("responseText", this.lightbox.get("contentBox").get("innerHTML"));
        },

        testLightbox: function() {
            this.lightbox.hide();
            Y.Assert.isFalse(this.lightbox.get("visible"));
            document.querySelector("a[rel^='lightbox']").click();
            Y.Assert.isTrue(this.lightbox.get("visible"));
            Y.Assert.areEqual("responseText", this.lightbox.get("contentBox").get("innerHTML"));
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(lightboxTestCase);
    Y.Test.Runner.masterSuite.name = "lightbox-test.js";
    Y.Test.Runner.run();

});
