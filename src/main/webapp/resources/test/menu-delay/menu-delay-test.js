"use strict";

var menuDelayTestCase = new Y.Test.Case({
    name: 'menu-delay Test Case',

    testMouseOver: function() {
        var menu = document.querySelector(".nav-menu");
        var content = document.querySelector(".nav-menu-content")
        var event = document.createEvent("UIEvent");//new UIEvent("mouseenter");
        event.initEvent("mouseenter", false, false);
        menu.dispatchEvent(event);
        Y.Assert.areSame("hidden", content.style.visibility);
        this.wait(function() {
            Y.Assert.areSame("visible", content.style.visibility);
        }, 600);
    }
});


document.querySelector("body").className = "yui3-skin-sam";
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(menuDelayTestCase);
Y.Test.Runner.masterSuite.name = "menu-delay.js";
Y.Test.Runner.run();
