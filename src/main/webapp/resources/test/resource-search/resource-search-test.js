/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y){
	
	Y.io = function(url, obj) {
		obj.on.success.apply(obj.context, ["id", {responseText:'{"resources": {"foo":{"hits":1},"bar":{"hits":2}}}'}]);
    };

    var resourceSearchTestCase = new Y.Test.Case({
        name: 'Lane ResourceSearch Test Case',
        search: Y.lane.ResourceSearch,
        result: null,
        
        testQueryMap: function() {
        	var handle = this.search.on("success", function(result) {
        		    this.result = result;
        		    handle.detach();
        	}, this);
        	this.search.search("mitral+valve", ["foo", "bar"]);
        	Y.Assert.areEqual(2, this.result.resources.bar.hits);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(resourceSearchTestCase);
    Y.Test.Runner.run();
});
