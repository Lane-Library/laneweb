/**
 * @author ceyates
 */
YUI({ logInclude: { TestRunner: true } }).use('event-custom', 'node-event-simulate','console','test', function(Y) {
    
    var laneTestCase = new Y.Test.Case({
        name: "Lane TestCase",
        testExists: function() {
            Y.Assert.isObject(LANE);
        },
        testNamespaceExists: function() {
            Y.Assert.isFunction(LANE.namespace);
        },
        testCoreExists: function() {
            Y.Assert.isObject(LANE.core);
        },
        testNamespace: function() {
            var o = LANE.namespace('LANE.newNamespace', 'another.newNamespace');
            Y.Assert.isObject(LANE.newNamespace);
            Y.Assert.isObject(LANE.another.newNamespace);
            Y.Assert.areSame(LANE.another.newNamespace, o);
        },
        testMetaValues: function() {
            Y.Assert.areEqual('1', LANE.core.getMetaContent('A'));
            Y.Assert.areEqual('2', LANE.core.getMetaContent('B'));
//        },
//        testHandleMouseOver: function() {
//            var d = document, p, e;
//            p = d.getElementById('p');
//            p.foo = 'foo';
//            p.activate = function() {
//                this.foo = 'bar';
//            };
//            YAHOO.util.UserAction.mouseover(p);
//            Y.Assert.areEqual('bar', p.foo);
//        },
//        testHandleMouseOut: function() {
//            var d = document, p, e;
//            p = d.getElementById('p');
//            p.foo = 'foo';
//            p.deactivate = function() {
//                this.foo = 'bar';
//            };
//            YAHOO.util.UserAction.mouseout(p);
//            Y.Assert.areEqual('bar', p.foo);
//        },
//        testHandleClick: function() {
//            var d = document, h, p, f, e;
//            h = d.documentElement;
//            p = d.getElementById('p');
//            f = function() {
//                this.foo = 'bar';
//            };
//            h.foo = 'foo';
//            h.clicked = f;
//            p.foo = 'foo';
//            p.clicked = f;
//            YAHOO.util.UserAction.click(p);
//            Y.Assert.areEqual('bar', h.foo);
//            Y.Assert.areEqual('bar', p.foo);
        }
    });
    
    
    var yconsole = new Y.Console({
        newestOnTop: false                   
    });
    yconsole.render('#log');
 
    
    Y.Global.on('lane:ready', function() {
        Y.Test.Runner.add(laneTestCase);
        Y.Test.Runner.run();
    });
});
