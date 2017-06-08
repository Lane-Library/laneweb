"use strict";

var locationTestCase = new Y.Test.Case({
    name: "Location TestCase",

    location: Y.lane.Location,

    testSetHref : function() {
        var currentHref = this.location.get("href");
        var newHref = "";
        var handle = this.location.on("hrefChange", function(event) {
            event.preventDefault();
            newHref = event.newVal;
        });
        this.location.set("href", "http://www.example.com");
        handle.detach();
        Y.Assert.areEqual("http://www.example.com", newHref);
        Y.Assert.areEqual(currentHref, this.location.get("href"));
    },

    testSetHash: function() {
        var currentHref = this.location.get("href");
        this.location.set("hash", "foo");
        Y.Assert.areEqual(currentHref + "#foo", this.location.get("href"));
        Y.Assert.areEqual("#foo", this.location.get("hash"));
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(locationTestCase);
Y.Test.Runner.masterSuite.name = "location-test.js";
Y.Test.Runner.run();
