/**
 * @author ceyates
 */
(function() {
    
    var expandiesTestCase = new YAHOO.tool.TestCase({
        name: "Lane Expandies TestCase",
        panel: {},
        testIsClosed: function() {
            panel = document.getElementById("panel1");
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
        },
        testExpanded: function() {
            panel = document.getElementById("panel2");
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
        },
        testExpand: function() {
            panel = document.getElementById("panel1");
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
            YAHOO.util.UserAction.click(panel.previousSibling);
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
            YAHOO.util.Dom.addClass(panel, 'hidden');
        },
        testClose: function() {
            panel = document.getElementById("panel2");
            YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
            YAHOO.util.UserAction.click(panel.previousSibling);
            this.wait(function() {
            YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel,'hidden'), 'className is ' + panel.className);
            YAHOO.util.Dom.removeClass(panel, 'hidden');
            }, 2000);
        },
        testAnchor: function() {
            panel = document.getElementById("panel3");
            if (document.location.hash == '#anchor') {
                YAHOO.util.Assert.isFalse(YAHOO.util.Dom.hasClass(panel, 'hidden'));
            } else {
                YAHOO.util.Assert.isTrue(YAHOO.util.Dom.hasClass(panel, 'hidden'));
            }
        }
        });

    new YAHOO.tool.TestLogger();
    YAHOO.tool.TestRunner.add(expandiesTestCase);
    YAHOO.util.Event.addListener(this, 'load', function() {
        YAHOO.tool.TestRunner.run();
    });
})();