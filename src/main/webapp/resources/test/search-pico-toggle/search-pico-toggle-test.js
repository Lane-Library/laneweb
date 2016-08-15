"use strict";

var searchPicoToggleTestCase = new Y.Test.Case({

    on: document.querySelector(".pico-on"),

    off: document.querySelector(".pico-off"),
    
    toggle: document.querySelector(".pico-toggle"),
    
    setUp: function() {
        this.on.className = "pico-on";
        this.off.className = "pico-off";
        this.toggle.className = "pico-toggle";
    },
    
    "test clinical search tab activated": function() {
        Y.lane.fire("searchTabs:change", {newVal: {source:"clinical-all"}});
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },
    
    "test not clinical search tab activated": function() {
        Y.lane.activate(this.on, "pico-on");
        Y.lane.activate(this.toggle, "pico-toggle");
        Y.lane.fire("searchTabs:change", {newVal: {source:"foo"}});
        Y.Assert.areEqual("pico-on", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle", this.toggle.className);
    },

    "test on click": function() {
        Y.lane.activate(this.on, "pico-on");
        Y.lane.activate(this.toggle, "pico-toggle");
        var active;
        Y.lane.once("picoToggle:change", function(event) {
            active = event.active;
        });
        var click = document.createEvent("MouseEvent");
        click.initEvent("click", true, false);
        this.on.dispatchEvent(click);
        Y.Assert.isTrue(active);
        Y.Assert.areEqual("pico-on", this.on.className);
        Y.Assert.areEqual("pico-off pico-off-active", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },

    "test off click": function() {
        Y.lane.activate(this.off, "pico-off");
        Y.lane.activate(this.toggle, "pico-toggle");
        var active;
        Y.lane.once("picoToggle:change", function(event) {
            active = event.active;
        });
        var click = document.createEvent("MouseEvent");
        click.initEvent("click", true, false);
        this.off.dispatchEvent(click);
        Y.Assert.isFalse(active);
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    },
    
    "test active change resets": function() {
        Y.lane.deactivate(this.on, "pico-on");
        Y.lane.activate(this.off, "pico-off");
        Y.lane.activate(this.toggle, "pico-toggle");
        Y.lane.fire("search:activeChange", {active: false});
        Y.Assert.areEqual("pico-on pico-on-active", this.on.className);
        Y.Assert.areEqual("pico-off", this.off.className);
        Y.Assert.areEqual("pico-toggle pico-toggle-active", this.toggle.className);
    }
});

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");

Y.Test.Runner.add(searchPicoToggleTestCase);
Y.Test.Runner.masterSuite.name = "search-pico-toggle-test.js";
Y.Test.Runner.run();
