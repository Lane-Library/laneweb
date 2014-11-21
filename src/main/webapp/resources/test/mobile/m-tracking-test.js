YUI().applyConfig({fetchCSS:true});
YUI().use('console','test', function(Y) {
    
    _gaq = {
         push : function(data){
             pushed = data
         }
    };
    
    var pushed;

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
            Y.Assert.areSame("_trackEvent", pushed[0]);
            Y.Assert.areSame("suggestSelect", pushed[1]);
            Y.Assert.isUndefined(pushed[2]);
            Y.Assert.areSame("undefined", pushed[3]);
        },
        
        "test track submit" : function() {
            $.LANE.tracking.track({target:{nodeName:"FORM"},srcElement:{nodeName:"FORM"},type:"submit"});
            Y.Assert.areSame("_trackPageview", pushed[0]);
            Y.Assert.areSame("/search?source=undefined&", pushed[1]);
            Y.Assert.isUndefined(pushed[2]);
            Y.Assert.isUndefined(pushed[3]);
        },
        
        "test track vclick" : function() {
            $.LANE.tracking.track({target:{nodeName:"LI",parentNode:{id:"searchTabs"}},srcElement:{nodeName:"LI",parentNode:{id:"searchTabs"}},type:"vclick"});
            Y.Assert.areSame("_trackEvent", pushed[0]);
            Y.Assert.areSame("searchTabClick", pushed[1]);
            Y.Assert.isUndefined(pushed[2]);
            Y.Assert.isUndefined(pushed[3]);
        },
        
        "test track hours click" : function() {
            $.LANE.tracking.track({
                target:{
                    nodeName:"H4",
                    parentNode:{
                        parentNode:{
                            id:"hours"
                        }
                    }
                },
                srcElement:{
                    nodeName:"H4",
                    parentNode:{
                        parentNode:{
                            id:"hours"
                        }
                    }
                }
            });
            Y.Assert.areSame("_trackPageview", pushed[0]);
            Y.Assert.areSame("/ONSITE/hours/close", pushed[1]);
            Y.Assert.isUndefined(pushed[2]);
            Y.Assert.isUndefined(pushed[3]);
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
