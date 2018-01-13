YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var mobileAdTestCase = new Y.Test.Case({

        name: 'mobile-ad Test Case',

    });

    new Y.Test.Console().render();


    Y.Test.Runner.add(mobileAdTestCase);
    Y.Test.Runner.masterSuite.name = "mobile-ad-test.js";
    Y.Test.Runner.run();

});
