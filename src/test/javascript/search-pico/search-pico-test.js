YUI({ fetchCSS: false }).use("test", "test-console", function (Y) {

    "use strict";

    let searchPicoTestCase = new Y.Test.Case({

        name: "Search Pico TestCase",

        p: document.querySelector("input[name=p]"),

        i: document.querySelector("input[name=i]"),

        q: document.querySelector("input[name=q]"),

        "test p to q": function () {
            this.p.value = "p";
            let e = document.createEvent("UIEvents");
            e.initEvent("input", true, false);
            this.p.dispatchEvent(e);
            Y.Assert.areEqual("p", this.q.value);
        },

        "test p and i to q": function () {
            this.p.value = "P";
            this.i.value = "I"
            let e = document.createEvent("UIEvents");
            e.initEvent("input", true, false);
            this.p.dispatchEvent(e);
            this.i.dispatchEvent(e);
            Y.Assert.areEqual("(P) AND (I)", this.q.value);
        }

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(searchPicoTestCase);
    Y.Test.Runner.masterSuite.name = "search-pico-test.js";
    Y.Test.Runner.run();

});
