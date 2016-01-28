"use strict";

YUI().use("*", function(Y) {
    
    var pushed;
    
    _gaq = {
         push : function(data){
             pushed.push(data);
         }
    };

    var laneMobileTestCase = new Y.Test.Case({
        name: "Lane Mobile TestCase",
        
        setUp: function() {
            pushed = [];
        },
        
        "test $.LANE.tracking exists" : function() {
            Y.Assert.isObject($.LANE.tracking);
        },
        
        "test track autocompleteselect" : function() {
            $.LANE.tracking.track({target:"",srcElement:"",type:"autocompleteselect"});
            Y.Assert.areSame("_trackEvent", pushed[0][0]);
            Y.Assert.areSame("suggestSelect", pushed[0][1]);
            Y.Assert.isUndefined(pushed[0][2]);
            Y.Assert.areSame("undefined", pushed[0][3]);
        },
        
        "test track submit" : function() {
            $("form").triggerHandler("submit");
            Y.Assert.areSame("_trackPageview", pushed[0][0]);
            Y.Assert.areSame("/search?source=action&qSearch=value", pushed[0][1]);
            Y.Assert.isUndefined(pushed[0][2]);
            Y.Assert.isUndefined(pushed[0][3]);
        },
        
        "test track vclick" : function() {
            var li = Y.Node.create("<ul class='searchTabs'><li>text</li></ul>").one("li")._node
            $.LANE.tracking.track({target:li,srcElement:li,type:"vclick"});
            Y.Assert.areSame("_trackEvent", pushed[0][0]);
            Y.Assert.areSame("searchTabClick", pushed[0][1]);
            Y.Assert.areSame("text", pushed[0][2]);
            Y.Assert.isUndefined(pushed[0][3]);
        },
        
        "test track hours click" : function() {
            var h4 = Y.Node.create("<div id='hours'><div><h4/></div></div>").one("h4")._node;
            $.LANE.tracking.track({
                target:h4,
                srcElement:h4,
                type:"click"
            });
            Y.Assert.areSame("_trackPageview", pushed[0][0]);
            Y.Assert.areSame("/ONSITE/hours/close", pushed[0][1]);
            Y.Assert.isUndefined(pushed[0][2]);
            Y.Assert.isUndefined(pushed[0][3]);
        },
        
        "test track hours click expanded" : function() {
            var h4 = Y.Node.create("<div id='hours' class='expanded'><div><h4/></div></div>").one("h4")._node;
            $.LANE.tracking.track({
                target:h4,
                srcElement:h4,
                type:"click"
            });
            Y.Assert.areSame("_trackPageview", pushed[0][0]);
            Y.Assert.areSame("/ONSITE/hours/open", pushed[0][1]);
            Y.Assert.isUndefined(pushed[0][2]);
            Y.Assert.isUndefined(pushed[0][3]);
        },
        
        "test track img click" : function() {
            var img = Y.Node.create("<a href='http://foo/bar'><img/></a>").one("img")._node;
            $.LANE.tracking.track({
                type:"click",
                target:img,
                srcElement:img
            });
            Y.Assert.areSame("_trackPageview", pushed[0][0]);
            Y.Assert.areSame("/OFFSITE/http%3A%2F%2Ffoo%2Fbar", pushed[0][1]);
            Y.Assert.isUndefined(pushed[0][2]);
            Y.Assert.isUndefined(pushed[0][3]);
        },
        
        "test track strong srcElement" : function() {
            var strong = Y.Node.create("<strong/>")._node;
            $.LANE.tracking.track({type:"click",srcElement:strong});
            Y.Assert.isUndefined(pushed[0]);
        },
        
        "test track strong target" : function() {
            var strong = Y.Node.create("<strong/>")._node;
            $.LANE.tracking.track({type:"click",target:strong});
            Y.Assert.isUndefined(pushed[0]);
        },
        
        "test ancestor has class ui-autocomplete" : function() {
            var autocomplete = Y.Node.create("<div class='ui-autocomplete'><div><a/></div></div>").one("a")._node;
            $.LANE.tracking.track({type:"click",target:autocomplete,srcElement:autocomplete});
            Y.Assert.isUndefined(pushed[0]);
        },
        
        "test click ranked" : function() {
            var ranked = Y.Node.create("<div rank='10'><a href='foo'>text</a></div>").one("a")._node;
            $.LANE.tracking.track({type:"click",target:ranked});
            Y.Assert.areSame("_trackEvent", pushed[0][0]);
            Y.Assert.areSame("searchResultClick", pushed[0][1]);
            Y.Assert.areSame("value", pushed[0][2]);
            Y.Assert.areSame("text", pushed[0][3]);
            Y.Assert.areSame(10, pushed[0][4]);
            Y.Assert.areSame("_trackPageview", pushed[1][0]);
            Y.Assert.areSame("/ONSITE/text", pushed[1][1]);
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
            Y.Assert.areSame("_trackPageview", pushed[0][0]);
            Y.Assert.areSame("/ONSITE/Hello!", pushed[0][1]);
        },
        
        "test simulate vclick" : function() {
            $("#a").trigger("vclick");
            Y.Assert.isUndefined(pushed[0]);
        }
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-tracking-test.js";
    Y.Test.Runner.run();
});
