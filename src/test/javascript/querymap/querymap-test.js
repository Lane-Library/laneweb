"use strict";

L.Model.set(L.Model.URL_ENCODED_QUERY, "foo%20bar");

L.io = function(url, config) {
    L.io = function(url,config) {
        L.io = function(url, config) {
            config.on.success.apply(this, [2, {
                                        responseText : JSON.stringify({
                                            "id" : "1849619284",
                                            "status" : "successful",
                                            "query" : "histrionic",
                                            "resources" : {
                                                "ovid-kaplan" : {
                                                    "status" : "successful",
                                                    "url" : "http://ovidsp.ovid.com/ovidweb.cgi?T=JS&PAGE=titles&D=books&SEARCH=histrionic.mp%0701412563.sc.%071+and+2",
                                                    "hits" : "67"
                                                },
                                                "am_ebert" : {
                                                    "status" : "successful",
                                                    "url" : "http://www.accessmedicine.com/search/searchAMResult.aspx?searchStr=histrionic&searchType=2&fullTextStr=histrionic&resourceID=10",
                                                    "hits" : "15"
                                                }
                                            }
                                        })
            }]);


            var querymapTestCase = new Y.Test.Case({
                name: 'querymap Test Case',

                testThatItWorked: function() {
                    Y.Assert.areEqual("Kaplan's Comprehensive Psychiatry: 67 Current Dx & Tx: Psychiatry: 15 ", Y.one("#queryMapping").get("text"));
                }
            });


            Y.one('body').addClass('yui3-skin-sam');
            new Y.Console({
                newestOnTop: false
            }).render('#log');


            Y.Test.Runner.add(querymapTestCase);
            Y.Test.Runner.masterSuite.name = "querymap.js";
            Y.Test.Runner.run();

        };
        config.on.success.apply(this, [1, {"responseText":
            JSON.stringify({
                                        "id" : "267196285",
                                        "status" : "running",
                                        "query" : "histrionic",
                                        "resources" : {
                                            "ovid-kaplan" : {
                                                "status" : "running",
                                                "url" : "http://ovidsp.ovid.com/ovidweb.cgi?T=JS&PAGE=titles&D=books&SEARCH=histrionic.mp%0701412563.sc.%071+and+2",
                                                "hits" : ""
                                            },
                                            "am_ebert" : {
                                                "status" : "running",
                                                "url" : "http://www.accessmedicine.com/search/searchAMResult.aspx?searchStr=histrionic&searchType=2&fullTextStr=histrionic&resourceID=10",
                                                "hits" : ""
                                            }
                                        }
                                    })
        }
        ]);
    };
    config.on.success.apply(this, [0, {"responseText":
        JSON.stringify({
            "descriptor" : {
                "descriptorName" : "Mental Disorders",
                "descriptorUI" : "D001523",
                "treeNumbers" : [ "F03" ]
            },
            "resources" : [ {
                "id" : "ovid-kaplan",
                "label" : "Kaplan\'s Comprehensive Psychiatry"
            }, {
                "id" : "am_ebert",
                "label" : "Current Dx & Tx: Psychiatry"
            } ]
        })
    } ]);
    
};
