(function() {
    "use strict";
    
    var toggle = document.querySelector(".clinical-toggle"),
        indicator = Y.lane.searchIndicator,
        circle = document.querySelector(".clinical-toggle-circle"),
        
        toggleClick = function() {
            indicator.show();
            if (circle.className.indexOf("peds") > -1) {
                circle.className = "clinical-toggle-circle";
            } else {
                circle.className = "clinical-toggle-circle clinical-toggle-circle-peds";
            }
        };
    
    if (toggle) {
        toggle.addEventListener("click", toggleClick);
    }
})();