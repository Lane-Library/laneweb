YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    L.Get.script = function(url, config) {
        
        window.gtag = function(...args) {
            if ("event" == arguments[0]) {
                googleTestCase.eventArgs = args;
            }
        };

        let googleTestCase = new Y.Test.Case({

            name: 'Lane Google Test Case',

            domainName: null,

            eventArgs: [],

            setUp: function() {
                this.eventArgs = [];
                this.pageviewArgs = [];
            },

            testTrackEvent: function() {
                L.fire("tracker:trackableEvent", {category:"category", action:"action", label:"label", value:"value"});
                Y.Assert.areSame("event", this.eventArgs[0]);
                Y.Assert.areSame("category", this.eventArgs[1]);
                Y.Assert.areSame("action", this.eventArgs[2]['event_action']);
                Y.Assert.areSame("label", this.eventArgs[2]['event_label']);
                Y.Assert.areSame("value", this.eventArgs[2]['event_value']);
            },

            testTrackPageview: function() {
                L.fire("tracker:trackablePageview",{title:"&title", path:"path"});
                Y.Assert.areSame("event", this.eventArgs[0]);
                Y.Assert.areSame("page_view", this.eventArgs[1]);
                Y.Assert.areSame("/ONSITE/%26title/path", this.eventArgs[2]['page_location']);
            },

            testTrackExternalPageview: function() {
                L.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path"});
                Y.Assert.areSame("event", this.eventArgs[0]);
                Y.Assert.areSame("lane:offsite", this.eventArgs[1]);
                Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[2]['event_action']);
                Y.Assert.areSame("hostpath", this.eventArgs[2]['event_label']);
            },

            testTrackExternalQueryPageview: function() {
                L.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path", query:"query"});
                Y.Assert.areSame("event", this.eventArgs[0]);
                Y.Assert.areSame("lane:offsite", this.eventArgs[1]);
                Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[2]['event_action']);
                Y.Assert.areSame("hostpathquery", this.eventArgs[2]['event_label']);
            }
        });

        config.onSuccess.apply(this);

        new Y.Test.Console().render();

        Y.Test.Runner.add(googleTestCase);
        Y.Test.Runner.masterSuite.name = "google-test.js";
        Y.Test.Runner.run();
    };

});
