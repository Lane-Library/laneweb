/**
 * @author ryanmax
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y){
    
    var skipTest = false, today = new Date();
    // Test will fail on Jan 1st before 9am so just skip running it
    if (today.getDate() == 1 && today.getMonth() == 0) {
        skipTest = true;
    }
    
    var seminarsTestCase = new Y.Test.Case({
        name: "Seminars TestCase",
        testBasic1: function() {
            if (skipTest) {
                return true;
            }
            var seminars = Y.all(".seminar");
            Y.Assert.areEqual(3, seminars.size());
            Y.Assert.areEqual("none", seminars.item(0).getStyle('display'));
            Y.Assert.areEqual("block", seminars.item(1).getStyle('display'));
            Y.Assert.areEqual("block", seminars.item(2).getStyle('display'));
    }
    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');


    Y.Test.Runner.add(seminarsTestCase);
    Y.Test.Runner.masterSuite.name = "seminars-test.js";
    Y.Test.Runner.run();
});
