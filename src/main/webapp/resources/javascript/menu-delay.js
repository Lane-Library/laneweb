/*
 * A simple script that delays the visibility of the drop downs for a short time
 */
(function() {

    "use strict";

    Array.prototype.forEach.call(document.querySelectorAll(".nav-menu"), function(node) {
        node.addEventListener("mouseenter", function(event) {
            var list = event.currentTarget.querySelector(".nav-menu-content");
            if (list) {
                list.style.visibility = "hidden";
                setTimeout(function() {
                    list.style.visibility = "visible";
                }, 500);
            }
        });
    });

})();