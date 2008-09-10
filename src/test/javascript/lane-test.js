/**
 * @author ceyates
 */
(function() {
var Assert = YAHOO.util.Assert;
var TestRunner = YAHOO.tool.TestRunner;
var TestCase = YAHOO.tool.TestCase;
var UserAction = YAHOO.util.UserAction;

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
    testSearchExists: function(){
        Assert.isObject(LANE.search);
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
        Assert.areEqual('1', LANE.core.getMetaContent('A'));
        Assert.areEqual('2', LANE.core.getMetaContent('B'));
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
    },
    testHover: function() {
        if (YAHOO.env.ua.ie) {
            var d = document, p, u;
            p = d.getElementById('otherPortalOptions');
            u = p.getElementsByTagName('ul')[0];
            YAHOO.util.UserAction.mouseover(p);
            Assert.areEqual('hover', p.className);
            YAHOO.util.UserAction.mouseout(p);
            Assert.areEqual('', p.className);
        }
    }
});

var LANESearchTestCase = new TestCase({
    name: "LaneSearch TestCase",
    testActivateDeactivateButton: function() {
        var d = document, s;
        s = d.getElementById('searchSubmit');
        Assert.areEqual('search_btn.gif', s.src.match(/search_btn.gif/));
        YAHOO.util.UserAction.mouseover(s);
        Assert.areEqual('search_btn_f2.gif', s.src.match(/search_btn_f2.gif/));
        YAHOO.util.UserAction.mouseout(s);
        Assert.areEqual('search_btn.gif', s.src.match(/search_btn.gif/));
    },
    testStartSearch: function() {
        var d = document, s = LANE.search, i, q;
        q = d.getElementById('p').getElementsByTagName('input')[0];
        i = d.getElementById('searchIndicator');
        Assert.areEqual('hidden', i.style.visibility);
        Assert.isFalse(s.isSearching());
        q.value = 'hello';
        s.startSearch();
        Assert.isTrue(s.isSearching());
        Assert.areEqual('visible', i.style.visibility);
        s.stopSearch();
        Assert.areEqual('hidden', i.style.visibility);
        q.value = '';
    },
    testStartSearchNoQuery: function() {
        var d = document, s = LANE.search;
        try {
            s.startSearch();
        } catch(ex) {
            Assert.areEqual('nothing to search for',ex.toString());
        }
    },
    //TODO: make this a real test, Assert something
    testToolTips: function() {
        var t = document.getElementById('toggleProxyOff');
        YAHOO.util.UserAction.mouseover(t);
        YAHOO.util.UserAction.mouseout(t);
    }
});
var oLogger = new YAHOO.tool.TestLogger();
TestRunner.add(LANETestCase);
TestRunner.add(LANECoreTestCase);
TestRunner.add(LANESearchTestCase);
TestRunner.run();
})();
