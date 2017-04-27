"use strict";

var submitted = false,

    searchTestCase = {

    name: "Lane Search Test Case",

    search: Y.lane.search,

    form: Y.one(".search-form"),

    queryInput: Y.one("input[name=q]"),

    sourceInput: Y.one("input[name=source]"),

    close: Y.one(".search-close"),

    setUp: function() {
        this.search.setSource("all-all");
        this.search.setQuery("");
        this.form.removeClass("search-form-active");
        submitted = false;
    },

    testExists: function() {
        Y.Assert.isTrue(Y.Lang.isObject(this.search));
    },

    testSetGetQuery: function() {
        this.search.setQuery("query");
        Y.Assert.areEqual("query", this.search.getQuery());
        Y.Assert.areEqual("query", this.queryInput.get("value"));
    },

    testQueryChangeEvent: function() {
        var value;
        this.search.once("queryChange", function() {
            value = this.getQuery();
        });
        this.search.setQuery("query");
        Y.Assert.areEqual("query", value);
    },

    testQueryChangeEventBubble: function() {
        var newVal, oldVal;
        Y.lane.once("search:queryChange", function(event) {
            newVal = event.newVal;
            oldVal = event.oldVal;
        });
        this.search.setQuery("query");
        Y.Assert.areEqual("", oldVal);
        Y.Assert.areEqual("query", newVal);
    },

    testSetQueryNull: function() {
        this.search.setQuery("query");
        this.search.setQuery(null);
        Y.Assert.areEqual("query", this.search.getQuery());
        Y.Assert.areEqual("query", this.queryInput.get("value"));
    },

    testSetQueryUndefined: function() {
        this.search.setQuery("query");
        this.search.setQuery(this.foo);
        Y.Assert.areEqual("query", this.search.getQuery());
        Y.Assert.areEqual("query", this.queryInput.get("value"));
    },

    testSetGetSource: function() {
        this.search.setSource("clinical-all");
        Y.Assert.areEqual("clinical-all", this.search.getSource());
        Y.Assert.areEqual("clinical-all", this.sourceInput.get("value"));
    },

    testSourceChangeEvent: function() {
        var value;
        this.search.once("sourceChange", function(event) {
            value = event.newVal;
        });
        this.search.setSource("bioresearch-all");
        Y.Assert.areEqual("bioresearch-all", value);
    },

    testSourceChangeEventBubble: function() {
        var newVal, oldVal;
        Y.lane.once("search:sourceChange", function(event) {
            newVal = event.newVal;
            oldVal = event.oldVal;
        });
        this.search.setSource("bioresearch-all");
        Y.Assert.areEqual("all-all", oldVal);
        Y.Assert.areEqual("bioresearch-all", newVal);
    },

    "test searchTabs:change event": function() {
        Y.lane.fire("searchTabs:change", {newVal: {source:"clinical-all"}});
        Y.Assert.areEqual("clinical-all", this.search.getSource());
        Y.Assert.areEqual("clinical-all", this.sourceInput.get("value"));
    },

    testSetSourceNull: function() {
        this.search.setSource("all-all");
        this.search.setSource(null);
        Y.Assert.areEqual("all-all", this.search.getSource());
        Y.Assert.areEqual("all-all", this.sourceInput.get("value"));
    },

    testSetSourceUndefined: function() {
        this.search.setSource("all-all");
        this.search.setSource(this.foo);
        Y.Assert.areEqual("all-all", this.search.getSource());
        Y.Assert.areEqual("all-all", this.sourceInput.get("value"));
    },

    testSearch: function() {
        var searched = false;
        this.search.once("search", function(event) {
            searched = true;
        });
        this.search.setQuery("query");
        this.search.search();
        Y.Assert.isTrue(searched);
        Y.Assert.isTrue(submitted);
    },

    testSearchNoQuery: function() {
        var searched = false;
        this.search.once("search", function(e) {
            searched = true;
        });
        this.search.search();
        Y.Assert.isFalse(searched);
        Y.Assert.isFalse(submitted);
    },

    testSearchEventBubble: function() {
        var searched = false;
        Y.lane.once("search:search", function(event) {
            searched = true;
        });
        this.search.setQuery("query");
        this.search.search();
        Y.Assert.isTrue(searched);
        Y.Assert.isTrue(submitted);
    },

    testInputFocus: function() {
        this.queryInput.simulate("focus");
        Y.Assert.isTrue(this.form.hasClass("search-form-active"));
    },

    testCloseClick: function() {
        this.form.addClass("search-form-active");
        this.close.simulate("click");
        Y.Assert.isFalse(this.form.hasClass("search-form-active"));
    },

    testSubmit: function() {
        var searched = false;
        this.search.once("search", function(event) {
            searched = true;
        });
        this.search.setQuery("query");
        Y.one("form").simulate("submit");
        Y.Assert.isTrue(searched);
        Y.Assert.isTrue(submitted);
    },

    testSubmitNoQuery: function() {
        var searched = false;
        this.search.once("search", function(event) {
            searched = true;
        });
        Y.one("form").simulate("submit");
        Y.Assert.isFalse(searched);
        Y.Assert.isFalse(submitted);
    },

    testInputChange: function() {
        this.queryInput.set("value", "query");
        var e = document.createEvent("UIEvents");
        e.initEvent("input", true, false);
        this.queryInput._node.dispatchEvent(e);
        Y.Assert.areEqual("query", this.search.getQuery());
    },

    "test reset clears query": function() {
        this.search.setQuery("query");
        Y.Assert.areEqual("query", this.queryInput.get("value"));
        Y.lane.fire("searchReset:reset");
        Y.Assert.areEqual("", this.search.getQuery());
    },

    "searchTabs:change submits if query": function() {
        this.search.setQuery("query");
        var searched = false;
        this.search.once("search", function(event) {
            searched = true;
        });
        Y.Assert.areEqual("query", this.queryInput.get("value"));
        Y.lane.fire("searchTabs:change", {newVal: {source:"foo"}});
        Y.Assert.isTrue(searched);
        Y.Assert.isTrue(submitted);
    },

    "searchTabs:change doesn't submit if no query": function() {
        this.search.setQuery("");
        var searched = false;
        this.search.once("search", function(event) {
            searched = true;
        });
        Y.Assert.areEqual("", this.queryInput.get("value"));
        Y.lane.fire("searchTabs:change", {newVal: {source:"foo"}});
        Y.Assert.isFalse(searched);
        Y.Assert.isFalse(submitted);
//    },
//
//    testPicoSearchActiveWithClinicalSource: function() {
//        Y.Assert.isFalse(this.pico.hasClass("active"));
//        this.search.setSource("clinical-all");
//        Y.Assert.isTrue(this.pico.hasClass("active"));
//    },
//
//    testDefaultPlaceholderCombined: function() {
//        this.queryInput.simulate("focus");
//        Y.Assert.isTrue(this.form.hasClass("active"));
//        this.queryInput.simulate("blur");
//        var property = this.queryInput.hasClass("inputHint") ? "value" : "placeholder";
//        Y.Assert.areEqual("all placeholder", this.queryInput.get(property));
//        this.close.simulate("click");
//        Y.Assert.isFalse(this.form.hasClass("active"));
//        Y.Assert.areEqual("all placeholder default placeholder", this.queryInput.get(property));
//    },
//
//    testTipChangesWithMouseover: function() {
//        Y.Assert.areEqual("all tip", this.tip.get("text"));
//        for (var i = 0; i < this.tabs.size(); i++) {
//            var tab = this.tabs.item(i);
//            tab.simulate("mouseover");
//            Y.Assert.areEqual(tab.get("title"), this.tip.get("text"));
//            tab.simulate("mouseout");
//            Y.Assert.areEqual("all tip", this.tip.get("text"));
//        }
//    },
//
//    testDefaultPlaceholderAfterReset: function() {
//        this.queryInput.simulate("blur");
//        var property = this.queryInput.hasClass("inputHint") ? "value" : "placeholder";
//        Y.Assert.areEqual("all placeholder default placeholder", this.queryInput.get(property));
//        this.queryInput.simulate("focus");
//        this.search.setQuery("query");
//        this.close.simulate("click");
//        this.reset.simulate("click");
//        Y.Assert.areEqual("all placeholder default placeholder", this.queryInput.get(property));
        
//      },
//      testSearchTipLinkChange: function() {
//      Y.Assert.isTrue(Y.one("#searchTips").get("href").indexOf("lanesearch.html") > 0 );
//      this.searchSource.set("selectedIndex",3);
//      this.searchSource.simulate("change");
//      Y.Assert.isTrue(Y.one("#searchTips").get("href").indexOf("bassettsearch.html") > 0 );
//      },
//      testSearchTermsHintChange: function() {
//      var placeholderCapable = "placeholder" in Y.Node.getDOMNode(Y.one("#searchTerms"));
//      var att = (placeholderCapable) ? "placeholder" : "value";
//      Y.Assert.areEqual("title1", Y.one("#searchTerms").get(att));
//      this.searchSource.set("selectedIndex",1);
//      this.searchSource.simulate("change");
//      Y.Assert.areEqual("title2", Y.one("#searchTerms").get(att));
//      },
//      testResetClickClearsInput: function() {
//      Y.one("#searchTerms").set("value","foo");
//      Y.one(".searchReset").simulate("click");
//      this.wait(function() {
//      Y.Assert.areEqual("", Y.one("#searchTerms").get("value"));
//      },1000);
//      },
//      testResetClickClearsFacetsAndSortParams: function() {
//      Y.one("#searchFields").append("<input type="hidden" name="facets" value="foo">");
//      Y.one("#searchFields").append("<input type="hidden" name="sort" value="foo">");
//      Y.one("#searchFields").append("<input type="hidden" name="other" value="foo">");
//      Y.one("#searchTerms").set("value","foo");
//      Y.Assert.areEqual(3, Y.all("#searchFields input[type=hidden]").size());
//      Y.one(".searchReset").simulate("click");
//      this.wait(function() {
//      Y.Assert.areEqual("", Y.one("#searchTerms").get("value"));
//      Y.Assert.areEqual(1, Y.all("#searchFields input[type=hidden]").size());
//      },1000);
//      },
//      testResetVisbleOnInputText: function() {
//      Y.one("#searchTerms").set("value","foo");
//      //TODO: fix this, the valueChange event doesn"t happen before checking the changed style
////    Y.Assert.areEqual("block", Y.one(".searchReset").getStyle("display"));
//      },
//      testReset: function() {
//      var reset = false;
//      this.handle = this.search.on("reset", function(event) {
//      reset = true;
//      });
//      Y.one(".searchReset").simulate("click");
//      Y.Assert.isTrue(reset);
//      },
//      testBubbleReset: function() {
//      var reset = false;
//      this.handle = Y.lane.on("reset", function(event) {
//      reset = true;
//      });
//      Y.one(".searchReset").simulate("click");
//      Y.Assert.isTrue(reset);
    }
};

document.querySelector(".search-form").submit = function() {
    submitted = true;
    console.log("/search.html?q=" + encodeURIComponent(Y.lane.search.getQuery()) + "&source=" + Y.lane.search.getSource());
};

Y.one("body").addClass("yui3-skin-sam");
new Y.Console({
    newestOnTop: false
}).render("#log");

Y.Test.Runner.add(new Y.Test.Case(searchTestCase));
Y.Test.Runner.masterSuite.name = "search-test.js";
Y.Test.Runner.run();
