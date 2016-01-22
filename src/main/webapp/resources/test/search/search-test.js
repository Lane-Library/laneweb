"use strict";

var searchTestCase = new Y.Test.Case({

    name: 'Lane Search Test Case',
    search: Y.lane.Search,

    searchTermsInput: Y.one('#searchTerms'),
    searchIndicator: Y.one('.searchIndicator'),
    searchSource: Y.one('#searchSource'),
    handle : null,

    setUp: function() {
        this.searchTermsInput.set('value', '');
        this.searchTermsInput.set('title', '');
        this.searchSource.set('selectedIndex',0);
        if (this.handle) {
            this.handle.detach();
            this.handle = null;
        }
        this.searchSource.simulate("change");
    },
    testSubmitSearchNoQuery: function() {
        var location = Y.lane.Location.get("href");
        this.search.submitSearch();
        Y.Assert.areEqual(location, Y.lane.Location.get("href"));
    },
    testSourceChange: function() {
        var value = null;
        this.handle = this.search.on('sourceChange', function(event) {
            value = event.newVal;
        });
        this.searchSource.set('selectedIndex',1);
        this.searchSource.simulate("change");
        Y.Assert.areEqual(this.searchSource.get('value'), value);
    },
    testSourceChangeBubble: function() {
        var value = null;
        this.handle = Y.lane.on('search:sourceChange', function(event) {
            value = event.newVal;
        });
        this.searchSource.set('selectedIndex',1);
        this.searchSource.simulate("change");
        Y.Assert.areEqual(value, this.searchSource.get('value'));
    },
    testGetSearchTerms: function() {
        Y.Assert.areEqual('', this.search.getSearchTerms());
    },
    testSetSearchTerms: function() {
        this.search.setSearchTerms('foo');
        Y.Assert.areEqual('foo', this.searchTermsInput.get('value'));
    },
    testSuggestSelect: function() {
        Y.publish("lane:suggestSelect",{broadcast:2});
        Y.fire("lane:suggestSelect");
    },
    testOnSubmit : function() {
        var submitted = false;
        this.handle = this.search.on("submit", function(event) {
            submitted = true;
            event.preventDefault();
        });
        this.search.submitSearch();
        Y.Assert.isTrue(submitted);
    },
    testBubbleOnSubmit : function() {
        var submitted = false;
        this.handle = Y.lane.on("search:submit", function(event) {
            submitted = true;
            event.preventDefault();
        });
        this.search.submitSearch();
        Y.Assert.isTrue(submitted);
    },
    testSearchTipLinkChange : function() {
        Y.Assert.isTrue(Y.one("#searchTips").get("href").indexOf("lanesearch.html") > 0 );
        this.searchSource.set('selectedIndex',3);
        this.searchSource.simulate("change");
        Y.Assert.isTrue(Y.one("#searchTips").get("href").indexOf("bassettsearch.html") > 0 );
    },
    testSearchTermsHintChange : function() {
        var placeholderCapable = 'placeholder' in Y.Node.getDOMNode(Y.one("#searchTerms"));
        var att = (placeholderCapable) ? "placeholder" : "value";
        Y.Assert.areEqual("title1", Y.one("#searchTerms").get(att));
        this.searchSource.set('selectedIndex',1);
        this.searchSource.simulate("change");
        Y.Assert.areEqual("title2", Y.one("#searchTerms").get(att));
    },
    testResetClickClearsInput : function() {
        Y.one("#searchTerms").set("value","foo");
        Y.one(".searchReset").simulate("click");
        this.wait(function() {
            Y.Assert.areEqual("", Y.one("#searchTerms").get("value"));
        },1000);
    },
    testResetClickClearsFacetsAndSortParams : function() {
        Y.one("#searchFields").append('<input type="hidden" name="facets" value="foo">');
        Y.one("#searchFields").append('<input type="hidden" name="sort" value="foo">');
        Y.one("#searchFields").append('<input type="hidden" name="other" value="foo">');
        Y.one("#searchTerms").set("value","foo");
        Y.Assert.areEqual(3, Y.all("#searchFields input[type=hidden]").size());
        Y.one(".searchReset").simulate("click");
        this.wait(function() {
            Y.Assert.areEqual("", Y.one("#searchTerms").get("value"));
            Y.Assert.areEqual(1, Y.all("#searchFields input[type=hidden]").size());
        },1000);
    },
    testResetVisbleOnInputText : function() {
        Y.one("#searchTerms").set("value","foo");
        //TODO: fix this, the valueChange event doesn't happen before checking the changed style
//      Y.Assert.areEqual("block", Y.one(".searchReset").getStyle("display"));
    },
    testReset : function() {
        var reset = false;
        this.handle = this.search.on("reset", function(event) {
            reset = true;
        });
        Y.one(".searchReset").simulate("click");
        Y.Assert.isTrue(reset);
    },
    testBubbleReset : function() {
        var reset = false;
        this.handle = Y.lane.on("search:reset", function(event) {
            reset = true;
        });
        Y.one(".searchReset").simulate("click");
        Y.Assert.isTrue(reset);
    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');

Y.Test.Runner.add(searchTestCase);
Y.Test.Runner.masterSuite.name = "search-test.js";
Y.Test.Runner.run();
