/**
 * @author ceyates
 */
Y.applyConfig({fetchCSS:true});
Y.use('node-event-simulate', 'console', 'test', function(Y) {

	var bannerTestCase = new Y.Test.Case({
		name : 'Banner Test Case'

	});

    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    Y.Test.Runner.add(bannerTestCase);
    Y.Test.Runner.run();
});
