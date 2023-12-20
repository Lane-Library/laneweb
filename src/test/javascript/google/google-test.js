YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    L.Get.script = function(url, config) {

        window.ga = function() {
            if ("pageview" == arguments[1]) {
                googleTestCase.pageviewArgs = arguments;
            }
            if ("event" == arguments[1]) {
                googleTestCase.eventArgs = arguments;
            }
        };
        
        let googleTestCase = new Y.Test.Case({

            name: 'Lane Google Test Case',

            domainName: null,

            eventArgs: [],

            pageviewArgs: [],

            setUp: function() {
                this.eventArgs = [];
                this.pageviewArgs = [];
            },

            testTrackEvent: function() {
                L.fire("tracker:trackableEvent", {category:"category", action:"action", label:"label", value:"value"});
                Y.Assert.areSame("send", this.eventArgs[0]);
                Y.Assert.areSame("event", this.eventArgs[1]);
                Y.Assert.areSame("category", this.eventArgs[2]);
                Y.Assert.areSame("action", this.eventArgs[3]);
                Y.Assert.areSame("label", this.eventArgs[4]);
                Y.Assert.areSame("value", this.eventArgs[5]);
            },

            testTrackPageview: function() {
                L.fire("tracker:trackablePageview",{title:"&title", path:"path"});
                Y.Assert.areSame("send", this.pageviewArgs[0]);
                Y.Assert.areSame("pageview", this.pageviewArgs[1]);
                Y.Assert.areSame("/ONSITE/%26title/path", this.pageviewArgs[2]);
            },

            testTrackExternalPageview: function() {
                L.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path"});
                Y.Assert.areSame("send", this.eventArgs[0]);
                Y.Assert.areSame("event", this.eventArgs[1]);
                Y.Assert.areSame("lane:offsite", this.eventArgs[2]);
                Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[3]);
                Y.Assert.areSame("hostpath", this.eventArgs[4]);
            },

            testTrackExternalQueryPageview: function() {
                L.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path", query:"query"});
                Y.Assert.areSame("send", this.eventArgs[0]);
                Y.Assert.areSame("event", this.eventArgs[1]);
                Y.Assert.areSame("lane:offsite", this.eventArgs[2]);
                Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[3]);
                Y.Assert.areSame("hostpathquery", this.eventArgs[4]);
            }
        });

        config.onSuccess.apply(this);

        new Y.Test.Console().render();

        Y.Test.Runner.add(googleTestCase);
        Y.Test.Runner.masterSuite.name = "google-test.js";
        Y.Test.Runner.run();
    };

});
