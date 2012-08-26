YUI({logInclude: { TestRunner: true } }).use('console','test', function(T) {
    
    var laneTestCase = new T.Test.Case({
        name: "Lane TestCase",
        "test LANE exists" : function() {
            T.Assert.isObject(LANE);
        },
        "test LANE.search exists": function() {
            T.Assert.isObject(LANE.search);
        },
        "test Y.lane exists" : function() {
        	T.Assert.isObject(Y.lane);
        }
    });
    
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false                   
    }).render('#log');
 
    
    T.Test.Runner.add(laneTestCase);
    T.Test.Runner.run();
});
