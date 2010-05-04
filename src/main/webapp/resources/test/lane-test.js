/**
 * @author ceyates
 */
YUI({ debug:true, filter:'debug', logInclude: { TestRunner: true } }).use('lane','console','test', function(Y) {
    
    var laneTestCase = new Y.Test.Case({
        name: "Lane TestCase",
        testExists: function() {
            Y.Assert.isObject(LANE);
        },
        testNamespaceExists: function() {
            Y.Assert.isFunction(LANE.namespace);
        },
        testNamespace: function() {
            var o = LANE.namespace('LANE.newNamespace', 'another.newNamespace');
            Y.Assert.isObject(LANE.newNamespace);
            Y.Assert.isObject(LANE.another.newNamespace);
            Y.Assert.areSame(LANE.another.newNamespace, o);
        }
    });
    
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false                   
    }).render('#log');
 
    
    Y.Test.Runner.add(laneTestCase);
    Y.Test.Runner.run();
});
