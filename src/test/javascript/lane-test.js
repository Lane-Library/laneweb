/**
 * @author ceyates
 */
LANE.core.initialize();
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
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
        p.activate = function() {
            this.style.color = 'red';
        };
        e = d.createEvent("MouseEvents");
        e.initMouseEvent("mouseover", true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, p);
        p.dispatchEvent(e);
        Assert.areEqual('red', p.style.color);
    },
    testHandleMouseOut: function(){
        var d = document, p, e;
        p = d.getElementById('p');
        p.deactivate = function() {
            this.style.color = 'blue';
        };
        e = d.createEvent("MouseEvents");
        e.initMouseEvent("mouseout", true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, p);
        p.dispatchEvent(e);
        Assert.areEqual('blue', p.style.color);
    },
    testHandleClick: function(){
        var d = document, h, p, f, e;
        h = d.documentElement;
        p = d.getElementById('p');
        f = function() {
            this.foo = 'bar';
        };
        h.foo = 'foo';
        h.clicked = f;
        p.foo = 'foo';
        p.clicked = f;
        e = d.createEvent("MouseEvents");
        e.initMouseEvent("click", true, true, window, 1, 0, 0, 0, 0, false, false, false, false, 0, p);
        p.dispatchEvent(e);
        Assert.areEqual('bar', h.foo);
        Assert.areEqual('bar', p.foo);
    }
});
var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETestCase);
TestRunner.add(LANECoreTestCase);
TestRunner.run();
