/**
 * @author ceyates
 */
YUI({ logInclude: { TestRunner: true } }).use('node-event-simulate','console','test', function(Y) {
    
    var expandiesTestCase = new Y.Test.Case({
        name: 'Lane Expandies TestCase',
        testIsClosed: function() {
            var panel = Y.one('#panel1');
            Y.Assert.isTrue(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
        },
        testExpanded: function() {
            var panel = Y.one('#panel2');
            Y.Assert.isFalse(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
        },
        testExpand: function() {
            var panel = Y.one('#panel1');
            Y.Assert.isTrue(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
            Y.Assert.isFalse(panel.get('parentNode').hasClass('expanded'));
            panel.get('previousSibling').simulate('click');
            Y.Assert.isFalse(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
            Y.Assert.isTrue(panel.get('parentNode').hasClass('expanded'));
        },
        testClose: function() {
            var panel = Y.one('#panel2');
            Y.Assert.isFalse(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
            panel.get('previousSibling').simulate('click');
            //delay this 500ms to allow expandy to close
            this.wait(function() {
                 Y.Assert.isTrue(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
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
            LANE.core.getChangeEvent().fire();
            Y.Assert.isTrue(panel.hasClass('yui-acc-hidden'), 'className is ' + panel.getAttribute('className'));
        }
    });
    
    var yconsole = new Y.Console({
        newestOnTop: false                   
    });
    yconsole.render('#log');
 
    
    Y.on('domready', function() {
        Y.Test.Runner.add(expandiesTestCase);
        Y.Test.Runner.run();
    });
});