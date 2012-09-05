//YUI.add("lane-model", function(Y){
(function() {
    
    Y.namespace("lane");
    
    var LANE = Y.lane,
    
    Model = function() {
        Model.superclass.constructor.apply(this, arguments);
    };
    
    Model.NAME = "model";
    
    Y.extend(Model, Y.Base);
    
    LANE.Model = new Model();
    
    LANE.Model.setAttrs(window.model || {});
    
})();
//},"", {
//    requires: ["base"]
//});