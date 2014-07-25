/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', "node-event-simulate", function(Y) {

    var bookmarkLinkTestCase = new Y.Test.Case({

        name : "BookmarkLink Test Case",

        link : Y.lane.BookmarkLink,

        setUp: function() {
            this.link.set("status", 0);
        },

        testExists : function() {
            Y.Assert.isTrue(Y.Lang.isObject(this.link));
        },

        testDontShowAlreadyBookmarked : function() {
            Y.one("#alreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        },

        testAnotherDontShowAlreadyBookmarked : function() {
            Y.one("#anotherAlreadyBookmarked").simulate("mouseover");
            Y.Assert.areEqual(0, this.link.get("status"));
        },

        testTargetMouseover: function() {
            var bookmarkable = Y.one("#bookmarkable");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(1, this.link.get("status"));
            Y.Assert.areSame(bookmarkable, this.link.get("target"));
        },

        testTargetBlock: function() {
            var bookmarkable = Y.one("#block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(0, this.link.get("status"));
        },

        testTargetInlineBlock: function() {
            var bookmarkable = Y.one("#inline-block");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(1, this.link.get("status"));
            Y.Assert.areSame(bookmarkable, this.link.get("target"));
        },

        testTargetMouseout: function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 1);
            bookmarkable.simulate("mouseout");
            Y.Assert.areSame(5, this.link.get("status"));
        },

        testNodeMouseover: function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 5);
            this.link.get("node").simulate("mouseover");
            Y.Assert.areSame(2, this.link.get("status"));
        },

        testNodeMouseout: function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            this.link.get("node").simulate("mouseout");
            Y.Assert.areSame(5, this.link.get("status"));
        },

        testClick: function() {
            var bookmarkable = Y.one("#bookmarkable");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            var self = this;
            var status = 0;
            var data = "";
            Y.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [0,{}]);
            };
            this.link.get("node").simulate("click");
            Y.Assert.areSame(3, status);
            Y.Assert.areSame(4, this.link.get("status"));
            Y.Assert.areSame('{"label":"bookmarkable","url":"http://www.example.com/"}', data);
        },

        testLocalClick: function() {
            var bookmarkable = Y.one("#local");
            this.link.set("target", bookmarkable);
            this.link.set("status", 2);
            var self = this;
            var data = "";
            var status = 0;
            Y.io = function(url, config) {
                status = self.link.get("status");
                data = config.data;
                config.on.success.apply(config.context, [0,{}]);
            };
            this.link.get("node").simulate("click");
            Y.Assert.areSame(3, status);
            Y.Assert.areSame(4, this.link.get("status"));
            Y.Assert.areSame('{"label":"local","url":"/foo?bar=baz"}', data);
        },

        testBookmarkSearchClick: function() {
            var data = "";
            Y.io = function(url, config) {
                data = config.data;
                config.on.success.apply(config.context, [0,{}]);
            };
            Y.one("#bookmarkSearch").simulate("click");
            Y.Assert.areSame('{"label":"Search for: query","url":"/search.html?source=undefined&q=undefined"}', data);
        },

        testTopResourceBookmarkable: function() {
            var bookmarkable = Y.one("#topResource");
            bookmarkable.simulate("mouseover");
            Y.Assert.areSame(1, this.link.get("status"));
            Y.Assert.areSame(bookmarkable, this.link.get("target"));
        },

        testTimer: function() {
            this.link.set("status", 5);
            this.wait(function() {
                Y.Assert.areSame(0, this.link.get("status"));
            }, this.link.get("hideDelay"));
        }

    });


    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(bookmarkLinkTestCase);
    Y.Test.Runner.masterSuite.name = "bookmark-link-test.js";
    Y.Test.Runner.run();
});
