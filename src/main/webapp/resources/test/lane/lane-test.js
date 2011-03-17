/**
 * @author ceyates
 */
YUI({logInclude: { TestRunner: true } }).use('console','test', function(T) {
    
    var laneTestCase = new T.Test.Case({
        name: "Lane TestCase",
        testExists: function() {
            T.Assert.isObject(Y.lane);
        },
        testImportNode: function() {
            T.Assert.isFunction(document.importNode);
        }
    });
    
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false                   
    }).render('#log');
 
    
    T.Test.Runner.add(laneTestCase);
    T.Test.Runner.run();
});
