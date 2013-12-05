/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){
    
    Y.io = function(url, config) {
        config.on.success.apply(this, [0, {responseText:"responseText"}]);
    };

    var lightboxTestCase = new Y.Test.Case({
        name: 'Lane Feedback Test Case',
        
        testLightbox: function() {
            Y.one("a").simulate("click");
            Y.Assert.areEqual("responseText", Y.one(".yui3-lightbox").get("text"));
        },
        
        testLightboxEsc: function() {
            Y.Assert.areEqual("visible", Y.one(".yui3-lightbox").getStyle("visibility"));
            Y.one("doc").simulate("keydown", { keyCode: 27 });
            Y.Assert.areEqual("hidden", Y.one(".yui3-lightbox").getStyle("visibility"));
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(lightboxTestCase);
    Y.Test.Runner.masterSuite.name = "lightbox-test.js";
    Y.Test.Runner.run();
});
