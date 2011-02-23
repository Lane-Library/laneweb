/**
 * @author ceyates
 */
YUI(
	{logInclude: {
	        TestRunner: true
	    }
	}).use('node', 'node-event-simulate', 'console', 'test', function(T){

    var expandiesTestCase = new T.Test.Case({
        name: 'Lane Expandies TestCase',
        
        testHasClassName : function() {
        	var expandies = T.all(".expandy");
        	for (var i = 0; i < expandies.size(); i++) {
        		T.Assert.isTrue(expandies.item(i).hasClass("yui3-accordion"));
        	}
        },
        
        testIsClosed: function() {
            var panel = T.one('#panel1');
            T.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
        },
        testExpanded: function() {
            var panel = T.one('#panel2');
            T.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
        },
        testExpand: function() {
            var panel = T.one('#panel1');
            T.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
            T.Assert.isFalse(panel.get('parentNode').hasClass('expanded'), 'parent class is expanded');
            panel.previous().simulate('click');
            this.wait(function() {
                T.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
                T.Assert.isTrue(panel.get('parentNode').hasClass('yui3-accordion-item-active'), 'parent class is not yui3-accordion-item-active');
            }, 500);
        },
        testClose: function() {
            var panel = T.one('#panel2');
            T.Assert.isFalse(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
            panel.previous().simulate('click');
            //delay this 500ms to allow expandy to close
            this.wait(function() {
                 T.Assert.isTrue(panel.get('clientHeight') === 0, 'height is ' + panel.get('clientHeight'));
                 }, 500);
        },
        testAnchor: function() {
            var panel = T.one('#panel3');
            if (document.location.hash == '#anchor') {
                T.Assert.isTrue(panel.get('parentNode').hasClass( 'yui3-accordion-item-active'));
            } else {
                T.Assert.isFalse(panel.get('parentNode').hasClass( 'yui3-accordion-item-active'));
            }
        },
        //TODO: disabling, do we need initialize on lane:change event?  any expandies in lane:change content?
//        testChangeEvent: function() {
//            var panel = T.one('#panel4');
//            var notExpandy = T.one('.not-expandy');
//            notExpandy.removeClass('not-expandy');
//            notExpandy.addClass('expandy');
//            T.fire('lane:change');
//            T.Assert.isTrue(panel.get('parentNode').hasClass('yui3-accordion-item-active'), 'className is ' +panel.get('parentNode').get("className"));
//        },
        testTriggerLinkIsNotTrigger: function() {
            var panel = T.one("#panel5");
            var link = T.one("#testLink");
            link.on("click", function(event) {event.preventDefault();});
            link.simulate("click");
            T.Assert.isFalse(panel.get('parentNode').hasClass('yui3-accordion-item-active'), "className is " + panel.get('parentNode').get("className"));
            
        },
        testTriggerLinkWithRel: function() {
            var panel = T.one("#panel6");
            var link = T.one("#testLinkRel");
            var clicked = false;
            link.on("click", function(event) {
                event.preventDefault();
                clicked = true;
            });
            link.simulate("click");
            T.Assert.isTrue(clicked);
            T.Assert.isFalse(panel.get('parentNode').hasClass('yui3-accordion-item-active'), "className is " + panel.get('parentNode').get("className"));
            
        }
    });
    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    
    T.Test.Runner.add(expandiesTestCase);
    T.Test.Runner.run();
});