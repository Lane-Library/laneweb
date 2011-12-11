/*
 * This attaches the Y object with all dependencies to the window
 * so we can use it object globally.  It also creates
 * the Y.lane object that is our local namespace.  The
 * LANE object is retained for backwards compatiblity
 * and the LANE.search object is created taking place of
 * the previous LANE.namespace function.
 */

YUI({fetchCSS:false}).use("*", function(Y) {
    
    //keep a global reference of this YUI object
    window.Y = Y;
    
    //create the lane namespace
    Y.namespace("lane");
    
    //create the LANE.search object that gets used elsewhere
    LANE = {
        search : {}
    };
    
});
