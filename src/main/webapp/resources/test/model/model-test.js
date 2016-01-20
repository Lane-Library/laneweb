Y.applyConfig({fetchCSS:true});
Y.use('console','test', function(Y) {


    var modelTestCase = new Y.Test.Case({

        name: 'Lane Model Test Case',
        model: Y.lane.Model,

        testGetValue : function() {
            Y.Assert.areEqual(this.model.get("key"), "value");
        },

        testSetGetValue: function() {
            this.model.set("key", "newvalue");
            Y.Assert.areEqual(this.model.get("key"), "newvalue");
        }

    });

    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');

    Y.Test.Runner.add(modelTestCase);
    Y.Test.Runner.masterSuite.name = "model-test.js";
    Y.Test.Runner.run();
});
