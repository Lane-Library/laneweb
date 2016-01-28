/*
 * A simple script that delays the visibility of the drop downs for a short time
 */
(function() {

    "use strict";

    Y.all(".nav-menu").on("mouseenter", function(event) {
        var list = event.currentTarget.one(".nav-menu-content");
        if (list) {
            list.setStyle("visibility", "hidden");
            setTimeout(function() {
                list.setStyle("visibility", "visible");
            }, 500);
        }
    });

})();