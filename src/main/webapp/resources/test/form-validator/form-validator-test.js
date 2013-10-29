Y.applyConfig({fetchCSS:true});
Y.use("lane-form-validator", 'node-event-simulate', 'console', 'test', function(Y){

    var form = Y.one("form"),
    
    formsTestCase = new Y.Test.Case({
        name: 'Lane Forms Test Case',
        
        form: Y.one("form"),
        
        testForClassIncorrect: function() {
            form.one("input[type='submit']").simulate("click");
            Y.Assert.isTrue(form.one("input").hasClass("incorrect"));
        },
        
        testForClassCorrect: function() {
            form.one("input").set("value", "foo");
            form.one("input[type='submit']").simulate("click");
            Y.Assert.isTrue(form.one("input").hasClass("correct"));
        }
    });
    
    (new Y.lane.FormValidator(form));
    
    form.on("submit", function(event) {
        event.preventDefault();
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(formsTestCase);
    Y.Test.Runner.masterSuite.name = "form-validator-test.js";
    Y.Test.Runner.run();
});