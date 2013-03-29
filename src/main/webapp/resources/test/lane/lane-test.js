Y.applyConfig({fetchCSS:true});
Y.use('console','test', function(Y) {
    
    var laneTestCase = new Y.Test.Case({
        name: "Lane TestCase",
        "test LANE exists" : function() {
            Y.Assert.isObject(LANE);
        },
        "test LANE.search exists": function() {
            Y.Assert.isObject(LANE.search);
        },
        "test Y.lane exists" : function() {
        	Y.Assert.isObject(Y.lane);
        }
    });
    
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
 
    
    Y.Test.Runner.add(laneTestCase);
    Y.Test.Runner.run();
});
