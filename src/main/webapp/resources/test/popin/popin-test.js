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
            Y.Assert.isTrue(Y.one("#popin").hasClass("active"));
            Y.Assert.areEqual("0px", Y.one("#searchResults").getStyle("margin-top"));
            Y.Assert.areEqual("7px", Y.one(".rightSearchTips").getStyle("margin-top"));
            Y.Assert.areEqual("-28px", Y.one("#searchFacets").getStyle("margin-top"));
        },
        
        testPopinEventIE8: function() {
            Y.UA.ie = 8;
            var node = Y.one("#queryMapping");
            node.setStyle("display", "inline");
            Y.fire("lane:popin", node);
            Y.Assert.isTrue(node.hasClass("active"));
            Y.Assert.isTrue(Y.one("#popin").hasClass("active"));
            Y.Assert.areEqual("-3px", Y.one("#searchResults").getStyle("margin-top"));
            Y.Assert.areEqual("7px", Y.one(".rightSearchTips").getStyle("margin-top"));
            Y.Assert.areEqual("-28px", Y.one("#searchFacets").getStyle("margin-top"));
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
