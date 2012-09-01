(function() {

	var Model = function() {
        Model.superclass.constructor.apply(this, arguments);
    };
    
    Model.NAME = "model";
    
	Y.extend(Model, Y.Base);
    
    Y.lane.Model = new Model();
    
    Y.lane.Model.setAttrs(window.model);
    
})();