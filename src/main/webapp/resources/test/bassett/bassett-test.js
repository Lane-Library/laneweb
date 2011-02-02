/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(Y){
	
	LANE.bassett.set("io", function(a, b, c) { 
		b.on.success(0, {responseText:"<a href='?foo=bar'>foo</a>"}, b.arguments);
		});

	var bassettTestCase = new Y.Test.Case({
		name : 'Lane Basset Test Case',

		testAccordeonLink : function() {
			Y.all('a').item(0).simulate('click');
		},

		testImageLink : function() {
			Y.one('#bassettContent').all('a').item(2).simulate('click');
		},

		testDiagramLink : function() {
			Y.one('#bassettContent').all('a').item(1).simulate('click');
		},

		testPhotoLink : function() {
			Y.one('#bassettContent').all('a').item(0).simulate('click');
		}

	});

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(bassettTestCase);
    Y.Test.Runner.run();
});
