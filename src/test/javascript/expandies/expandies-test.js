"use strict";

var expandiesTestCase = new Y.Test.Case({
    name: 'Lane Expandies TestCase',
    testIsClosed: function() {
        var panel = Y.one('#panel1');
        Y.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
    },
    testExpanded: function() {
        var panel = Y.one('#panel2');
        Y.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
    },
    testExpand: function() {
        var panel = Y.one('#panel1');
        Y.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
        Y.Assert.isFalse(panel.get('parentNode').hasClass('expanded'), 'parent class is expanded');
        panel.previous().simulate('click');
        this.wait(function() {
            Y.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
            Y.Assert.isTrue(panel.get('parentNode').hasClass('yui3-accordion-item-active'), 'parent class is not yui3-accordion-item-active');
        }, 500);
    },
    testClose: function() {
        var panel = Y.one('#panel2');
        Y.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
        panel.previous().simulate('click');
        //delay this 500ms to allow expandy to close
        this.wait(function() {
            Y.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
        }, 500);
    },
    testAnchor: function() {
        var panel = Y.one('#panel3');
        if (Y.lane.Location.get("hash") == '#anchor') {
            Y.Assert.isTrue(panel.get('parentNode').hasClass( 'yui3-accordion-item-active'));
        } else {
            Y.Assert.isFalse(panel.get('parentNode').hasClass( 'yui3-accordion-item-active'));
        }
    },
    testTriggerLinkIsNotTrigger: function() {
        var panel = Y.one("#panel5");
        var link = Y.one("#testLink");
        link.on("click", function(event) {event.preventDefault();});
        link.simulate("click");
        Y.Assert.isFalse(panel.get('parentNode').hasClass('yui3-accordion-item-active'), "className is " + panel.get('parentNode').get("className"));

    },
    testTriggerLinkWithRel: function() {
        var panel = Y.one("#panel6");
        var link = Y.one("#testLinkRel");
        var clicked = false;
        link.on("click", function(event) {
            event.preventDefault();
            clicked = true;
        });
        link.simulate("click");
        Y.Assert.isTrue(clicked);
        Y.Assert.isFalse(panel.get('parentNode').hasClass('yui3-accordion-item-active'), "className is " + panel.get('parentNode').get("className"));

    }
});

Y.one('body').addClass('yui3-skin-sam');
new Y.Console({
    newestOnTop: false
}).render('#log');


Y.Test.Runner.add(expandiesTestCase);
Y.Test.Runner.masterSuite.name = "expandies-test.js";
Y.Test.Runner.run();
