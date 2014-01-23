/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){

    var popinTestCase = new Y.Test.Case({
        name: 'Lane Popin Test Case',

        testPopinEvent: function() {
            var node = Y.one("#spellCheck");
            node.setStyle("display", "inline");
            Y.fire("lane:popin", node);
            Y.Assert.isTrue(node.hasClass("active"));
            if (!Y.UA.ie || Y.UA.ie <= 7) {
                Y.Assert.isTrue(Y.one("#popin").hasClass("active"));
                Y.Assert.areEqual("0px", Y.one("#searchResults").getStyle("marginTop"));
            } else {
                Y.Assert.isTrue(Y.one("#popin").hasClass("active"));
                Y.Assert.areEqual("-3px", Y.one("#searchResults").getStyle("marginTop"));
            }
            Y.Assert.areEqual("7px", Y.one(".rightSearchTips").getStyle("marginTop"));
            if (!Y.UA.ie || Y.UA.ie > 6) {
                Y.Assert.areEqual("-28px", Y.one("#searchFacets").getStyle("marginTop"));
            } else {
                Y.Assert.areEqual("0px", Y.one("#searchFacets").getStyle("marginTop"));
            }
        },

        testPopinEventIE8: function() {
            Y.UA.ie = 8;
            var node = Y.one("#queryMapping");
            node.setStyle("display", "inline");
            Y.fire("lane:popin", node);
            Y.Assert.isTrue(node.hasClass("active"));
            Y.Assert.isTrue(Y.one("#popin").hasClass("active"));
            Y.Assert.areEqual("-3px", Y.one("#searchResults").getStyle("marginTop"));
            Y.Assert.areEqual("7px", Y.one(".rightSearchTips").getStyle("marginTop"));
            Y.Assert.areEqual("-28px", Y.one("#searchFacets").getStyle("marginTop"));
        }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(popinTestCase);
    Y.Test.Runner.masterSuite.name = "popin-test.js";
    Y.Test.Runner.run();
});
