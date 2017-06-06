"use strict";

YUI().use("*", function(Y) {

    var laneMobileTestCase = new Y.Test.Case({
        name: "Lane Mobile TestCase",
        "test $.LANE exists" : function() {
            Y.Assert.isObject($.LANE);
        },
        
        "test autocomplete off" : function() {
            $(":input[data-type=search]").each(function(){
                Y.Assert.areEqual("off", $(this).attr("autocomplete"));
            });
        },
        
        "test autocomplete enable" : function() {
            $(":input[data-type=search]").each(function(){
                var disabled = $(this).autocomplete("option", "disabled");
                $(this).trigger("focus");
                $(this).trigger("blur");
                Y.Assert.isFalse(disabled);
            });
        },
        
        "test keypress" : function() {
            var ajax = $.ajax;
            var foo = $("input[name='qSearch']").data("uiAutocomplete");
            $.ajax = function(o) {
                o.success.apply({autocompleteRequest:1}, [{suggest:["item1","item2"]}])
            }
            foo.source({term:"term"}, function(o) {
                Y.Assert.areSame(o[0].label, "item1");
            });
            Y.ajax = ajax;
//        },
//        
//        "test autocompleteselect" : function() {
//          var history = window.history.replaceState;
//          window.history.relaceState = function(){}
//          $("input[name='qSearch']").trigger("autocompleteselect", {item:{label:"label",value:"value"}});
//          window.history.relaceState = history;
        },
        
        "test AC limit" : function() {
            var acLimit = $.LANE.getACLimit;
            Y.Assert.areSame("mesh-d", acLimit($("<input id='condition'/>")));
            Y.Assert.areSame("mesh-i", acLimit($("<input id='intervention'/>")));
            Y.Assert.areSame("mesh-di", acLimit($("<input id='comparison'/>")));
            Y.Assert.areSame("mesh-di", acLimit($("<input id='clinical'/>")));
            Y.Assert.areSame("mesh-di", acLimit($("<input id='ped'/>")));
            Y.Assert.areSame("Book", acLimit($("<input id='book'/>")));
            Y.Assert.areSame("Journal", acLimit($("<input id='journal'/>")));
            Y.Assert.areSame("er-mesh", acLimit($("<input id='foo'/>")));
        }
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-suggest-test.js";
    Y.Test.Runner.run();
});
