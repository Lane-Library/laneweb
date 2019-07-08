YUI({fetchCSS:false}).use("test", "test-console", function(Y) {

    "use strict";

    var pageviewArgs;

    var eventArgs;

    $.ajax = function() {
        
        window.ga = function() {
            if ("pageview" == arguments[1]) {
                laneMobileTestCase.pageviewArgs = arguments;
            }
            if ("event" == arguments[1]) {
                laneMobileTestCase.eventArgs = arguments;
            }
        };
    };

    var laneMobileTestCase = new Y.Test.Case({
        name: "Lane Mobile TestCase",
        
        eventArgs: [],

        pageviewArgs: [],

        setUp: function() {
            this.eventArgs = [];
            this.pageviewArgs = [];
        },

        "test $.LANE.tracking exists" : function() {
            Y.Assert.isObject($.LANE.tracking);
        },
        
        "test track autocompleteselect" : function() {
            $.LANE.tracking.track({target:"",srcElement:"",type:"autocompleteselect"});
            Y.Assert.areSame("event", this.eventArgs[1]);
            Y.Assert.areSame("suggestSelect", this.eventArgs[2]);
            Y.Assert.isUndefined(this.eventArgs[3]);
            Y.Assert.areSame("undefined", this.eventArgs[4]);
        },
        
        "test track submit" : function() {
            $("form").triggerHandler("submit");
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/search?source=action&qSearch=value", this.pageviewArgs[2]);
            Y.Assert.isUndefined(this.pageviewArgs[3]);
            Y.Assert.isUndefined(this.pageviewArgs[4]);
        },
        
        "test track vclick" : function() {
            var li = Y.Node.create("<ul class='searchTabs'><li>text</li></ul>").one("li")._node
            $.LANE.tracking.track({target:li,srcElement:li,type:"vclick"});
            Y.Assert.areSame("event", this.eventArgs[1]);
            Y.Assert.areSame("searchTabClick", this.eventArgs[2]);
            Y.Assert.areSame("text", this.eventArgs[3]);
            Y.Assert.isUndefined(this.eventArgs[4]);
        },
        
        "test track hours click" : function() {
            var h4 = Y.Node.create("<div id='hours'><div><h4/></div></div>").one("h4")._node;
            $.LANE.tracking.track({
                target:h4,
                srcElement:h4,
                type:"click"
            });
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/ONSITE/hours/close", this.pageviewArgs[2]);
            Y.Assert.isUndefined(this.pageviewArgs[3]);
            Y.Assert.isUndefined(this.pageviewArgs[4]);
        },
        
        "test track hours click expanded" : function() {
            var h4 = Y.Node.create("<div id='hours' class='expanded'><div><h4/></div></div>").one("h4")._node;
            $.LANE.tracking.track({
                target:h4,
                srcElement:h4,
                type:"click"
            });
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/ONSITE/hours/open", this.pageviewArgs[2]);
            Y.Assert.isUndefined(this.pageviewArgs[3]);
            Y.Assert.isUndefined(this.pageviewArgs[4]);
        },
        
        "test track img click" : function() {
            var img = Y.Node.create("<a href='http://foo/bar'><img/></a>").one("img")._node;
            $.LANE.tracking.track({
                type:"click",
                target:img,
                srcElement:img
            });
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/OFFSITE/http%3A%2F%2Ffoo%2Fbar", this.pageviewArgs[2]);
            Y.Assert.isUndefined(this.pageviewArgs[3]);
            Y.Assert.isUndefined(this.pageviewArgs[4]);
        },
        
        "test track strong srcElement" : function() {
            var strong = Y.Node.create("<strong/>")._node;
            $.LANE.tracking.track({type:"click",srcElement:strong});
            Y.Assert.isUndefined(this.pageviewArgs[1]);
        },
        
        "test track strong target" : function() {
            var strong = Y.Node.create("<strong/>")._node;
            $.LANE.tracking.track({type:"click",target:strong});
            Y.Assert.isUndefined(this.pageviewArgs[1]);
        },
        
        "test ancestor has class ui-autocomplete" : function() {
            var autocomplete = Y.Node.create("<div class='ui-autocomplete'><div><a/></div></div>").one("a")._node;
            $.LANE.tracking.track({type:"click",target:autocomplete,srcElement:autocomplete});
            Y.Assert.isUndefined(this.pageviewArgs[1]);
        },
        
        "test click ranked" : function() {
            var ranked = Y.Node.create("<div rank='10'><a href='foo'>text</a></div>").one("a")._node;
            $.LANE.tracking.track({type:"click",target:ranked});
            Y.Assert.areSame("event", this.eventArgs[1]);
            Y.Assert.areSame("searchResultClick", this.eventArgs[2]);
            Y.Assert.areSame("value", this.eventArgs[3]);
            Y.Assert.areSame("text", this.eventArgs[4]);
            Y.Assert.areSame(10, this.eventArgs[5]);
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/ONSITE/text", this.pageviewArgs[2]);
        },
        
        "test getTrackingTitle title unknown" : function() {
            var title = Y.Node.create("<a/>")._node;
            Y.Assert.areSame("unknown", $.LANE.tracking.getTrackingTitle(title));
        },
        
        "test getTrackingTitle title attribute" : function() {
            var title = Y.Node.create("<a title='title'/>")._node;
            Y.Assert.areSame("title", $.LANE.tracking.getTrackingTitle(title));
        },
        
        "test getTrackingTitle alt attribute" : function() {
            var title = Y.Node.create("<img alt='title'/>")._node;
            Y.Assert.areSame("title", $.LANE.tracking.getTrackingTitle(title));
        },
        
        "test getTrackingTitle img alt" : function() {
            var title = Y.Node.create("<a><img alt='title'/></a>")._node;
            Y.Assert.areSame("title", $.LANE.tracking.getTrackingTitle(title));
        },
        
        "test getTrackingTitle img not alt" : function() {
            var title = Y.Node.create("<a><img/></a>")._node;
            Y.Assert.areSame("unknown", $.LANE.tracking.getTrackingTitle(title));
        },
        
        "test simulate click" : function() {
            $("#a").trigger("click");
            Y.Assert.areSame("pageview", this.pageviewArgs[1]);
            Y.Assert.areSame("/ONSITE/Hello!", this.pageviewArgs[2]);
        },
        
        "test simulate vclick" : function() {
            $("#a").trigger("vclick");
            Y.Assert.isUndefined(this.pageviewArgs[1]);
        }
    });

    new Y.Test.Console().render();

    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-tracking-test.js";
    Y.Test.Runner.run();

});
