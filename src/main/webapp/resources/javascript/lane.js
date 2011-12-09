/**
 * @author ceyates
 */
YUI({fetchCSS:false}).use("*", function(Y) {
    
    //keep a global reference of this YUI object
    window.Y = Y;
    
    //create the lane namespace
    Y.namespace("lane");
    
    if (typeof LANE == "undefined" || !LANE) {
        /**
         * The LANE global namespace object.  If LANE is already defined, the
         * existing LANE object will not be overwritten so that defined
         * namespaces are preserved.
         * @class LANE
         * @static
         */
        LANE = {};
    }
    LANE.namespace = function() {
        var a = arguments, o = null, i, j, d;
        for (i = 0; i < a.length; i = i + 1) {
            d = a[i].split(".");
            o = LANE;
            // LANE is implied, so it is ignored if it is included
            for (j = (d[0] == "LANE") ? 1 : 0; j < d.length; j = j + 1) {
                o[d[j]] = o[d[j]] || {};
                o = o[d[j]];
            }
        }
        return o;
    };
    
});
