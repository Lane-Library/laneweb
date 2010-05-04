/**
 * @author ceyates
 */YUI({
     gallery: 'gallery-2010.04.02-17-26'
}).use('gallery-node-accordion', 'node', 'node-event-simulate', 'console', 'test', function(Y){

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
                Y.Assert.isTrue(panel.get('parentNode').hasClass('expanded'), 'parent class is not expanded');
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
            if (document.location.hash == '#anchor') {
                Y.Assert.isFalse(panel.hasClass( 'yui-acc-hidden'));
            } else {
                Y.Assert.isTrue(panel.hasClass( 'yui-acc-hidden'));
            }
        },
        testChangeEvent: function() {
            var panel = Y.one('#panel4');
            var notExpandy = Y.one('.not-expandy');
            notExpandy.removeClass('not-expandy');
            notExpandy.addClass('expandy');
            Y.fire('lane:change');
            Y.Assert.isTrue(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(expandiesTestCase);
    Y.Test.Runner.run();
});