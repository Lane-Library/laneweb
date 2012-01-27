(function() {

	var conf = {}, node = Y.one("#model"),
    Model = function() {
        Model.superclass.constructor.apply(this, arguments);
    };
    
    Model.NAME = "model";
    
    Model.ATTRS = {
        sunetid : {
        	value : null
        },
        bookmarks : {
            value : null
        }
    };
    
	Y.extend(Model, Y.Base);
    
    if (node) {
    	conf = Y.JSON.parse(node.get("innerHTML"));
    }
    Y.lane.Model = new Model(conf);
    
    
})();