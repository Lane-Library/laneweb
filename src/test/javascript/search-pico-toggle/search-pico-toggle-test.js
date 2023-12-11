YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

"use strict";

let searchPicoToggleTestCase = new Y.Test.Case({

    on: document.querySelector(".pico-on"),

    off: document.querySelector(".pico-off"),
    
    toggle: document.querySelector(".pico-toggle"),
    
    setUp: function() {
        this.on.className = "pico-on";
        this.off.className = "pico-off";
        this.toggle.className = "pico-toggle";
    },
    
    "test clinical search tab activated": function() {
        L.fire("searchDropdown:change", {newVal: {source:"clinical-all"}});
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },
    
    "test not clinical search tab activated": function() {
        this.on.classList.add("pico-on-active");
        this.toggle.classList.add("pico-toggle-active");
        L.fire("searchDropdown:change", {newVal: {source:"foo"}});
        Y.Assert.areEqual("pico-on", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle", this.toggle.className);
    },

    "test on click": function() {
        this.on.classList.add("pico-on-active");
        this.toggle.classList.add("pico-toggle-active");
        let active;
        L.once("picoToggle:change", function(event) {
            active = event.active;
        });
        let click = document.createEvent("MouseEvent");
        click.initEvent("click", true, false);
        this.on.dispatchEvent(click);
        Y.Assert.isTrue(active);
        Y.Assert.areEqual("pico-on", this.on.className);
        Y.Assert.areEqual("pico-off pico-off-active", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },

    "test off click": function() {
        this.off.classList.add("pico-off-active");
        this.toggle.classList.add("pico-toggle-active");
        let active;
        L.once("picoToggle:change", function(event) {
            active = event.active;
        });
        let click = document.createEvent("MouseEvent");
        click.initEvent("click", true, false);
        this.off.dispatchEvent(click);
        Y.Assert.isFalse(active);
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },
    
    "test active change resets": function() {
        this.on.classList.remove("pico-on-active");
        this.off.classList.add("pico-off-active");
        this.toggle.classList.add("pico-toggle-active");
        L.fire("search:activeChange", {active: false});
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    }
});

new Y.Test.Console().render();

Y.Test.Runner.add(searchPicoToggleTestCase);
Y.Test.Runner.masterSuite.name = "search-pico-toggle-test.js";
Y.Test.Runner.run();

});
