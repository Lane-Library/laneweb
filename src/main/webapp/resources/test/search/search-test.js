/**
 * @author ceyates
 */
//Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate','console','test', function(T) {


    var searchTestCase = new T.Test.Case({

        name: 'Lane Search Test Case',
        search: Y.lane.Search,

        searchTermsInput: T.one('#searchTerms'),
        searchIndicator: T.one('.searchIndicator'),
        searchSource: T.one('#searchSource'),
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
            T.Assert.areEqual(location, Y.lane.Location.get("href"));
        },
        testSourceChange: function() {
            var value = null;
            this.handle = this.search.on('sourceChange', function(event) {
                value = event.newVal;
            });
            this.searchSource.set('selectedIndex',1);
            this.searchSource.simulate("change");
            T.Assert.areEqual(this.searchSource.get('value'), value);
        },
        testSourceChangeBubble: function() {
            var value = null;
            this.handle = Y.lane.on('search:sourceChange', function(event) {
                value = event.newVal;
            });
            this.searchSource.set('selectedIndex',1);
            this.searchSource.simulate("change");
            T.Assert.areEqual(value, this.searchSource.get('value'));
        },
        testGetSearchTerms: function() {
            T.Assert.areEqual('', this.search.getSearchTerms());
        },
        testSetSearchTerms: function() {
            this.search.setSearchTerms('foo');
            T.Assert.areEqual('foo', this.searchTermsInput.get('value'));
        },
        testSuggestSelect: function() {
            T.publish("lane:suggestSelect",{broadcast:2});
            T.fire("lane:suggestSelect");
        },
        testOnSubmit : function() {
            var submitted = false;
            this.handle = this.search.on("submit", function(event) {
                submitted = true;
                event.preventDefault();
            });
            this.search.submitSearch();
            T.Assert.isTrue(submitted);
        },
        testBubbleOnSubmit : function() {
            var submitted = false;
            this.handle = Y.lane.on("search:submit", function(event) {
                submitted = true;
                event.preventDefault();
            });
            this.search.submitSearch();
            T.Assert.isTrue(submitted);
        },
        testSearchTipLinkChange : function() {
            T.Assert.isTrue(T.one("#searchTips").get("href").indexOf("lanesearch.html") > 0 );
            this.searchSource.set('selectedIndex',3);
            this.searchSource.simulate("change");
            T.Assert.isTrue(T.one("#searchTips").get("href").indexOf("bassettsearch.html") > 0 );
        },
        testSearchTermsHintChange : function() {
            var placeholderCapable = 'placeholder' in Y.Node.getDOMNode(Y.one("#searchTerms"));
            var att = (placeholderCapable) ? "placeholder" : "value";
            T.Assert.areEqual("title1", Y.one("#searchTerms").get(att));
            this.searchSource.set('selectedIndex',1);
            this.searchSource.simulate("change");
            T.Assert.areEqual("title2", Y.one("#searchTerms").get(att));
        },
        testResetClickClearsInput : function() {
            T.one("#searchTerms").set("value","foo");
            T.one(".searchReset").simulate("click");
            this.wait(function() {
                T.Assert.areEqual("", T.one("#searchTerms").get("value"));
            },1000);
        },
        testResetVisbleOnInputText : function() {
            Y.one("#searchTerms").set("value","foo");
            //TODO: fix this, the valueChange event doesn't happen before checking the changed style
//            T.Assert.areEqual("block", Y.one(".searchReset").getStyle("display"));
        },
        testReset : function() {
            var reset = false;
            this.handle = this.search.on("reset", function(event) {
                reset = true;
            });
            T.one(".searchReset").simulate("click");
            T.Assert.isTrue(reset);
        },
        testBubbleReset : function() {
            var reset = false;
            this.handle = Y.lane.on("search:reset", function(event) {
                reset = true;
            });
            T.one(".searchReset").simulate("click");
            T.Assert.isTrue(reset);
        }
    });

    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');

    T.Test.Runner.add(searchTestCase);
    T.Test.Runner.masterSuite.name = "search-test.js";
    T.Test.Runner.run();
});
