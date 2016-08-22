"use strict";

Y.lane.search = {};

var teletypeTestCase = new Y.Test.Case({
    name: 'Lane Teletype Test Case',

    searchTerms: Y.one("input[name=q]"),
    teletypeInput: Y.one("#teletypeInput"),
    clinicalP: Y.one("#clinicalP"),
    picoTeletypeInput: Y.one("#picoTeletypeInput"),
    submitted: false,

    submit: function() {
        teletypeTestCase.submitted = true;
    },


    setUp: function() {
        this.searchTerms.set("value","");
        this.teletypeInput.set("value","");
        this.clinicalP.set("value","");
        this.picoTeletypeInput.set("value","");
        this.submitted = false;
        Y.lane.search.submit = this.submit;
    },

    testTeletypeSearchTerms: function() {
        this.teletypeInput.set("value","value");
        this.teletypeInput.simulate("keyup");
        Y.Assert.areEqual("value", this.searchTerms.get("value"));
    },

    testTeletypePico: function() {
        this.picoTeletypeInput.set("value", "value");
        this.picoTeletypeInput.simulate("keyup");
        Y.Assert.areEqual("value", this.clinicalP.get("value"));
    },

    testBlurTeletypeSearchTerms: function() {
        this.teletypeInput.set("value","value");
        this.teletypeInput.simulate("blur");
        //TODO: IE doesn't seem to want to simulate blur
        if (!Y.UA.ie) {
            Y.Assert.areEqual("value", this.searchTerms.get("value"));
        }
    },

    testBlurTeletypePico: function() {
        this.picoTeletypeInput.set("value", "value");
        this.picoTeletypeInput.simulate("blur");
        //TODO: IE doesn't seem to want to simulate blur
        if (!Y.UA.ie) {
            Y.Assert.areEqual("value", this.clinicalP.get("value"));
        }
    },

    testSubmit: function() {
        Y.one("form").simulate("submit");
        Y.Assert.isTrue(this.submitted);
    },

    testInputValueEqualsTitle: function() {
        this.teletypeInput.set("value", this.teletypeInput.get("title"));
        this.teletypeInput.simulate("keyup");
        Y.Assert.areEqual(this.searchTerms.get("title"), this.searchTerms.get("value"));
    },

    testSubmitThrowsError: function() {
        var alert = window.alert;
        var message = "";
        window.alert = function(string) {
            message = string;
        };
        Y.lane.search.submit = function() {
            throw("error");
        };
        Y.one("form").simulate("submit");
        window.alert = alert;
        Y.Assert.areEqual("error", message);
    }
});


Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(teletypeTestCase);
Y.Test.Runner.masterSuite.name = "teletype-test.js";
Y.Test.Runner.run();
