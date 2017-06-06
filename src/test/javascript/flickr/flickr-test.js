YUI().use("test", "node", "console", function(Y) {
    "use strict";

var flickrTestCase = new Y.Test.Case({
    name: 'Flickr Test Case'
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(flickrTestCase);
Y.Test.Runner.masterSuite.name = "flickr-test.js";
Y.Test.Runner.run();
});
