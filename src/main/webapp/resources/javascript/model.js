//YUI.add("lane-model", function(Y){
(function() {
    
    Y.namespace("lane");
    
    var LANE = Y.lane,
    
    Model = function() {
        Model.superclass.constructor.apply(this, arguments);
    };
    
    Y.extend(Model, Y.Base, {
    	//keep this in sync with edu.stanford.irt.laneweb.model.Model
    	AUTH : "auth",
    	BASE_PATH : "base-path",
    	DISASTER_MODE : "disaster-mode",
    	IPGROUP : "ipgroup",
    	QUERY : "query",
    	SOURCE : "source",
    	URL_ENCODED_QUERY : "url-encoded-query"
    }, {
    	NAME : "model"
    });
    
    LANE.Model = new Model();
    
    LANE.Model.setAttrs(window.model || {});
    
})();
//},"", {
//    requires: ["base"]
//});