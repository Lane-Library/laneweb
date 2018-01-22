YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var flickrTestCase = new Y.Test.Case({

        name: 'Flickr Test Case'

    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(flickrTestCase);
    Y.Test.Runner.masterSuite.name = "flickr-test.js";
    Y.Test.Runner.run();

});
