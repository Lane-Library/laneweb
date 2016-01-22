"use strict";

var bassettTestCase = new Y.Test.Case({
    name : 'Lane Basset Test Case',

    testAccordeonLink : function() {
        // Y.all('a').item(0).simulate('click');
    },

    testImageLink : function() {
        // Y.one('#bassettContent').all('a').item(2).simulate('click');
    },

    testDiagramLink : function() {
        // Y.one('#bassettContent').all('a').item(1).simulate('click');
    },

    testPhotoLink : function() {
        // Y.one('#bassettContent').all('a').item(0).simulate('click');
    }

});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop : false
}).render('#log');

Y.Test.Runner.add(bassettTestCase);
Y.Test.Runner.masterSuite.name = "bassett-test.js";
Y.Test.Runner.run();
