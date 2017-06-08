"use strict";

var mobileAdTestCase = new Y.Test.Case({
    name: 'mobile-ad Test Case'
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(mobileAdTestCase);
Y.Test.Runner.masterSuite.name = "mobile-ad-test.js";
Y.Test.Runner.run();
