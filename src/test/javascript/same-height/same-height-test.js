YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    let sameHeightTestCase = new Y.Test.Case({

        name: 'same-height Test Case',

        testSameHeight: function() {
            let divs1 = Y.all(".same-height-1");
            let div1 = Y.one(".same-height-1");
            let divs2 = Y.all(".same-height-2");
            let div2 = Y.one(".same-height-2");
            Y.Assert.areNotEqual(div1.get("clientHeight"), div2.get("clientHeight"));
            for (let i = 0; i < divs1.size(); i++) {
                Y.Assert.areEqual(div1.get("clientHeight"), divs1.item(i).get("clientHeight"));
            }
            for (let i = 0; i < divs2.size(); i++) {
                Y.Assert.areEqual(div2.get("clientHeight"), divs2.item(i).get("clientHeight"));
            }

        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(sameHeightTestCase);
    Y.Test.Runner.masterSuite.name = "same-height.js";
    Y.Test.Runner.run();

});
