"use strict";

Y.Get.script = function(url, config) {

    window._gat = {
            _createTracker : function() {
                return {
                    _setDomainName: function(name) {
                        googleTestCase.domainName = name;
                    },
                    _trackPageview: function() {
                        googleTestCase.pageArgs = arguments;
                    },
                    _trackEvent: function() {
                        googleTestCase.eventArgs = arguments;
                    }
                };
            }
    };

    var googleTestCase = new Y.Test.Case({
        name: 'Lane Google Test Case',
        domainName: null,
        eventArgs: [],
        pageArgs: [],
        setUp: function() {
            this.eventArgs = [];
            this.pageArgs = [];
        },
        testDomainName: function() {
            Y.Assert.areEqual(".stanford.edu", this.domainName);
        },
        testTrackEvent: function() {
            Y.lane.fire("tracker:trackableEvent", {category:"category", action:"action", label:"label", value:"value"});
            Y.Assert.areSame("category", this.eventArgs[0]);
            Y.Assert.areSame("action", this.eventArgs[1]);
            Y.Assert.areSame("label", this.eventArgs[2]);
            Y.Assert.areSame("value", this.eventArgs[3]);
        },
        testTrackPageview: function() {
            Y.lane.fire("tracker:trackablePageview",{title:"&title", path:"path"});
            Y.Assert.areSame("/ONSITE/%26title/path", this.pageArgs[0]);
        },
        testTrackExternalPageview: function() {
            Y.lane.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path"});
            Y.Assert.areSame("/OFFSITE/%26title", this.pageArgs[0]);
            Y.Assert.areSame("lane:offsite", this.eventArgs[0]);
            Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[1]);
            Y.Assert.areSame("hostpath", this.eventArgs[2]);
        },
        testTrackExternalQueryPageview: function() {
            Y.lane.fire("tracker:trackablePageview",{external:true,title:"&title", host:"host", path:"path", query:"query"});
            Y.Assert.areSame("/OFFSITE/%26title", this.pageArgs[0]);
            Y.Assert.areSame("lane:offsite", this.eventArgs[0]);
            Y.Assert.areSame("/OFFSITE-CLICK-EVENT/%26title", this.eventArgs[1]);
            Y.Assert.areSame("hostpathquery", this.eventArgs[2]);
        }
    });

    config.onSuccess.apply(this);

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(googleTestCase);
    Y.Test.Runner.masterSuite.name = "google-test.js";
    Y.Test.Runner.run();
};
