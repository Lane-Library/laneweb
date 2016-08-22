"use strict";

var searchPicoTestCase = new Y.Test.Case({

    name: "Search Pico TestCase",
    
    p: document.querySelector("input[name=p]"),
    
    i: document.querySelector("input[name=i]"),
    
    q: document.querySelector("input[name=q]"),
    
    "test p to q": function() {
        this.p.value = "p";
        var e = document.createEvent("UIEvents");
        if (Y.UA.ie === 9) {
            e.initEvent("keyup", true, false);
        } else {
            e.initEvent("input", true, false); 
        }
        this.p.dispatchEvent(e);
        Y.Assert.areEqual("p", this.q.value);
    },
    
    "test p and i to q": function() {
        this.p.value = "P";
        this.i.value = "I"
        var e = document.createEvent("UIEvents");
        if (Y.UA.ie === 9) {
            e.initEvent("keyup", true, false);
        } else {
            e.initEvent("input", true, false); 
        }
        this.p.dispatchEvent(e);
        this.i.dispatchEvent(e);
        Y.Assert.areEqual("(P) AND (I)", this.q.value);
    }

});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(searchPicoTestCase);
Y.Test.Runner.masterSuite.name = "search-pico-test.js";
Y.Test.Runner.run();
