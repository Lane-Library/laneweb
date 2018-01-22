YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

"use strict";

Y.Test.Runner.add(new Y.Test.Case({

    name: "Search Pico Fields TestCase",
    
    picoFields: null,
    
    setUp: function() {
        this.picoFields = document.querySelector(".pico-fields");
    },
    
    tearDown: function() {
        this.picoFields.className = "pico-fields";
    },
    
    "test lane:activeChange active:false": function() {
        this.picoFields.className = "pico-fields pico-fields-active";
        L.fire("search:activeChange", {active:false});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields");
    },
    
    "test lane:activeChange active:true": function() {
        L.fire("search:activeChange", {active:true});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields");
    },
    
    "test searchTabs:change source:'all-all'": function() {
        this.picoFields.className = "pico-fields pico-fields-active";
        L.fire("searchTabs:change", {newVal:{source:"all-all"}});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields");
    },
    
    "test searchTabs:change source:'clinical-all'": function() {
        L.fire("searchTabs:change", {newVal:{source:"clinical-all"}});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields");
    },
    
    "test picoToggle:change active:true": function() {
        L.fire("picoToggle:change", {active:true});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields pico-fields-active");
    },
    
    "test picoToggle:change active:false": function() {
        this.picoFields.className = "pico-fields pico-fields-active";
        L.fire("picoToggle:change", {active:false});
        Y.Assert.isTrue(this.picoFields.className === "pico-fields");
    }

}));

new Y.Test.Console().render();

Y.Test.Runner.masterSuite.name = "search-pico-fields-test.js";
Y.Test.Runner.run();

});