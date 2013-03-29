/**
 * @author ceyates
 */

Y.applyConfig({fetchCSS:true});
Y.use('console', 'test', function(Y){
	
	Y.io = function(url, obj) {
		obj.on.success.apply(obj.context, ["id", {responseText:'{"descriptor":null,"frequencies":null,"query":"mitral valve","resourceMap":{"descriptor":{"descriptorName":"Heart Diseases","descriptorUI":"D006331","treeNumbers":["C14.280"]},"resources":[{"id":"mdc_braunwald","label":"Braunwald\'s Heart Disease"}]},"treePath":null}'}]);
	};

    var querymapTestCase = new Y.Test.Case({
        name: 'Lane Querymap Test Case',
        mapper: Y.lane.QueryMapper,
        queryMap: null,
        
        testQueryMap: function() {
        	var handle = this.mapper.on("success", function(queryMap) {
        		    this.queryMap = queryMap;
        		    handle.detach();
        	}, this);
        	this.mapper.getQueryMap("mitral+valve");
        	Y.Assert.areEqual("mdc_braunwald", this.queryMap.resourceMap.resources[0].id);
        }
    });
    
    Y.one('body').addClass('yui3-skin-sam');
    new Y.Console({
        newestOnTop: false
    }).render('#log');
    
    
    Y.Test.Runner.add(querymapTestCase);
    Y.Test.Runner.run();
});
