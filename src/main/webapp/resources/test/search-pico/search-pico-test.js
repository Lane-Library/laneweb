"use strict";

var searchpicoTestCase = new Y.Test.Case({
    name: "Lane Search PICO Testcase",

    testSourceChangeClinical : function() {
        Y.lane.fire('search:sourceChange', {
            newVal:'clinical-all',
            oldVal:'all-all'
        });
        var nav = Y.one('.lane-nav');
        var search = Y.one('#search');
        Y.Assert.isTrue(Y.Lang.isObject(Y.one('.picoFields')), 'no pico fields');
    },
    testSourceChangeNotClinical : function() {
        Y.lane.fire('search:sourceChange', {
            newVal:'all-all',
            oldVal:'clinical-all'
        });
        var nav = Y.one('.lane-nav');
        var search = Y.one('#search');
        var picoFields = Y.one(".picoFields");
        Y.Assert.isFalse(picoFields.hasClass("active"));
    },
    testSetPatientCondition: function() {
        //simulate blur broken in IE http://yuilibrary.com/projects/yui3/ticket/2531702
        if (!Y.UA.ie) {
            Y.lane.fire('search:sourceChange', {
                newVal:'clinical-all',
                oldVal:'all-all'
            });
            var clinicalP = Y.one("#clinicalP");
            var clinicalI = Y.one("#clinicalI");
            var searchTerms = Y.one("#searchTerms");
            clinicalP.set("value", "foo");
            clinicalP.simulate("blur");
            Y.Assert.areEqual("foo", searchTerms.get("value"));
            clinicalI.set("value","bar");
            clinicalI.simulate("blur");
            Y.Assert.areEqual("(foo) AND (bar)", searchTerms.get("value"));
        }
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(searchpicoTestCase);
Y.Test.Runner.masterSuite.name = "search-pico-test.js";
Y.Test.Runner.run();
