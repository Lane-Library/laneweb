/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var ieTestCase = new Y.Test.Case({
        name: 'Lane IE Test Case',

        testNavParentHasClassHover: function() {
            var menu = Y.one("#laneNavMenu");
            menu.simulate("mouseover");
            Y.Assert.isTrue(menu.hasClass("hover"));
            menu.simulate("mouseout");
            Y.Assert.isFalse(menu.hasClass("hover"));
        },

        testFavoritesClassHover: function() {
            var menu = Y.one("#favorites");
            menu.simulate("mouseover");
            Y.Assert.isTrue(menu.hasClass("hover"));
            menu.simulate("mouseout");
            Y.Assert.isFalse(menu.hasClass("hover"));
        },

        testLibraryContextHasNoBorder: function() {
            var node = Y.one("#libraryContact").one("li");
            var style = node.getStyle("borderLeft");
            Y.Assert.isTrue(style.indexOf("none") >= 0 || style === "currentColor");
        },

        testSpanAfterLastInModule: function() {
            var node = Y.one(".module");
            Y.Assert.isTrue(node.get("lastChild").hasClass("after"));
        },

        testSpanAfterNextTopResources: function() {
            var node = Y.one("#topResources");
            Y.Assert.isTrue(node.next().hasClass("after"));
        },

        testBookmarkletNotIEDisplayNone: function() {
            Y.Assert.areEqual("none", Y.one("#bookmarkletNotIE").getStyle("display"));
        },

        testBookmarkletIEDisplayBlock: function() {
            Y.Assert.areEqual("block", Y.one("#bookmarkletIE").getStyle("display"));
        }


    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(ieTestCase);
    Y.Test.Runner.masterSuite.name = "ie-test.js";
    Y.Test.Runner.run();
});
