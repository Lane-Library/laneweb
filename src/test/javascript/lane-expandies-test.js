/**
 * @author ceyates
 */
(function() {
    
    var expandiesTestCase = new YAHOO.tool.TestCase({
        name: "Lane Expandies TestCase",
        panel: {},
        testIsClosed: function() {
            panel = document.getElementById("panel1");
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
        },
        testExpanded: function() {
            panel = document.getElementById("panel2");
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
        },
        testExpand: function() {
            panel = document.getElementById("panel1");
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel.parentNode, 'expanded'));
            YAHOO.util.UserAction.click(panel.previousSibling);
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel.parentNode, 'expanded'));
            YAHOO.util.Dom.addClass(panel, 'yui-acc-hidden');
        },
        testClose: function() {
            panel = document.getElementById("panel2");
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
            YAHOO.util.UserAction.click(panel.previousSibling);
            //delay this 500ms to allow expandy to close
            this.wait(function() {
                 YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
                 YAHOO.util.Dom.removeClass(panel, 'yui-acc-hidden');
                 }, 500);
        },
        testAnchor: function() {
            panel = document.getElementById("panel3");
            if (document.location.hash == '#anchor') {
                YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel, 'yui-acc-hidden'));
            } else {
                YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel, 'yui-acc-hidden'));
            }
        },
        testChangeEvent: function() {
            var panel = document.getElementById('panel4');
            var notExpandy = YAHOO.util.Dom.getElementsByClassName('not-expandy')[0];
            YAHOO.util.Dom.addClass(notExpandy, 'expandy');
            LANE.core.getChangeEvent().fire();
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'yui-acc-hidden'), 'className is ' + panel.className);
        }
    });

    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(expandiesTestCase);
    YAHOO.util.Event.addListener(this, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();