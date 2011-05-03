/**
 * @author ceyates
 */
YUI({
    logInclude: {
        TestRunner: true
    }
}).use('node-event-simulate', 'console', 'test', function(T) {

	var bannerTestCase = new T.Test.Case({
		name : 'Banner Test Case'

	});

    
    T.one('body').addClass('yui3-skin-sam');
    new T.Console({
        newestOnTop: false
    }).render('#log');
    
    T.Test.Runner.add(bannerTestCase);
    T.Test.Runner.run();
});
