YUI().applyConfig({fetchCSS:true});
YUI().use('console','test',"dump", function(Y) {

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
        }
    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(laneMobileTestCase);
    Y.Test.Runner.masterSuite.name = "m-lane-test.js";
    Y.Test.Runner.run();
});
