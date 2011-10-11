/*
 * A simple script that delays the visibility of the drop downs for a short time
 */
(function() {
    
    Y.all("#laneNav>li").on("mouseenter", function(event) {
        event.currentTarget.one("ul").setStyle("visibility","hidden");
        setTimeout(function() {
            event.currentTarget.one("ul").setStyle("visibility", "visible");
        }, 500);
    });
    
    Y.all("#somNav>li").on("mouseenter", function(event) {
        event.currentTarget.one("ul").setStyle("visibility","hidden");
        setTimeout(function() {
            event.currentTarget.one("ul").setStyle("visibility", "visible");
        }, 600);
    });

})();