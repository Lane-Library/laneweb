/**
 * @author ceyates
 */
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
var UserAction = YAHOO.util.UserAction;

LANE.core.initialize();

var LANETestCase = new TestCase({
    name: "Lane TestCase",
    testExists: function(){
        Assert.isObject(LANE);
    },
    testNamespaceExists: function(){
        Assert.isFunction(LANE.namespace);
    },
    testCoreExists: function(){
        Assert.isObject(LANE.core);
    },
    testNamespace: function(){
        var o = LANE.namespace('LANE.newNamespace', 'another.newNamespace');
        Assert.isObject(LANE.newNamespace);
        Assert.isObject(LANE.another.newNamespace);
        Assert.areSame(LANE.another.newNamespace, o);
    }
});
var LANECoreTestCase = new TestCase({
    name: "LaneCore TestCase",
    testMetaValues: function(){
        Assert.areEqual('1', LANE.core.meta.A);
        Assert.areEqual('2', LANE.core.meta.B);
    },
    testHandleMouseOver: function(){
        var d = document, p, e;
        p = d.getElementById('p');
        p.foo = 'foo';
        p.activate = function(){
            this.foo = 'bar';
        };
        YAHOO.util.UserAction.mouseover(p);
        Assert.areEqual('bar', p.foo);
    },
    testHandleMouseOut: function(){
        var d = document, p, e;
        p = d.getElementById('p');
        p.foo = 'foo';
        p.deactivate = function(){
            this.foo = 'bar';
        };
        YAHOO.util.UserAction.mouseout(p);
        Assert.areEqual('bar', p.foo);
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
        Assert.areEqual('bar', h.foo);
        Assert.areEqual('bar', p.foo);
    }
});
var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETestCase);
TestRunner.add(LANECoreTestCase);
TestRunner.run();
