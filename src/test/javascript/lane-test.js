/**
 * @author ceyates
 */
(function(){
    var laneTestCase = new YAHOO.tool.TestCase({
        name: "Lane TestCase",
        testExists: function(){
            YAHOO.util.Assert.isObject(LANE);
        },
        testNamespaceExists: function(){
            YAHOO.util.Assert.isFunction(LANE.namespace);
        },
        testCoreExists: function(){
            YAHOO.util.Assert.isObject(LANE.core);
        },
        testNamespace: function(){
            var o = LANE.namespace('LANE.newNamespace', 'another.newNamespace');
            YAHOO.util.Assert.isObject(LANE.newNamespace);
            YAHOO.util.Assert.isObject(LANE.another.newNamespace);
            YAHOO.util.Assert.areSame(LANE.another.newNamespace, o);
        },
        testMetaValues: function(){
            YAHOO.util.Assert.areEqual('1', LANE.core.getMetaContent('A'));
            YAHOO.util.Assert.areEqual('2', LANE.core.getMetaContent('B'));
        },
        testHandleMouseOver: function(){
            var d = document, p, e;
            p = d.getElementById('p');
            p.foo = 'foo';
            p.activate = function(){
                this.foo = 'bar';
            };
            YAHOO.util.UserAction.mouseover(p);
            YAHOO.util.Assert.areEqual('bar', p.foo);
        },
        testHandleMouseOut: function(){
            var d = document, p, e;
            p = d.getElementById('p');
            p.foo = 'foo';
            p.deactivate = function(){
                this.foo = 'bar';
            };
            YAHOO.util.UserAction.mouseout(p);
            YAHOO.util.Assert.areEqual('bar', p.foo);
        },
        testHandleClick: function(){
            var d = document, h, p, f, e;
            h = d.documentElement;
            p = d.getElementById('p');
            f = function(){
                this.foo = 'bar';
            };
            h.foo = 'foo';
            h.clicked = f;
            p.foo = 'foo';
            p.clicked = f;
            YAHOO.util.UserAction.click(p);
            YAHOO.util.Assert.areEqual('bar', h.foo);
            YAHOO.util.Assert.areEqual('bar', p.foo);
        }
    });
    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(laneTestCase);
    YAHOO.tool.TestRunner.run();
})();
