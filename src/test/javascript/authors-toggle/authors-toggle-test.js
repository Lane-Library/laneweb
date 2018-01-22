YUI({fetchCSS:false}).use("test", "test-console", "node-event-simulate", function(Y) {

    "use strict";

    var authorsToggleTestCase = new Y.Test.Case({

        name: "authors toggle Test Case",

        testToggleAuthors: function() {
            var trigger = Y.one(".authorsTrigger"), 
            parent = trigger.get('parentNode'),
            hiddenAuthors = parent.one('.authors-hide');

            Y.Assert.areEqual(hiddenAuthors.getStyle('display'), 'none');
            trigger.simulate('click');
            Y.Assert.areNotEqual(hiddenAuthors.getStyle('display'), 'none');
            Y.Assert.areEqual('Show Less', trigger.get("text").trim());
            trigger.simulate('click');
            Y.Assert.areEqual(hiddenAuthors.getStyle('display'), 'none');
            Y.Assert.areEqual('Show More', trigger.get("text").trim());
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(authorsToggleTestCase);
    Y.Test.Runner.masterSuite.name = "authors-toggle-test.js";
    Y.Test.Runner.run();

});
