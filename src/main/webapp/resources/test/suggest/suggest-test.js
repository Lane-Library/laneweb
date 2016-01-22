"use strict";

var suggestTestCase = new Y.Test.Case({
    name: "Lane Suggest Testcase"
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(suggestTestCase);
Y.Test.Runner.masterSuite.name = "suggest-test.js";
Y.Test.Runner.run();
