(function() {

    "use strict";

    document.querySelectorAll(".golfclub").forEach(function(node) {
        node.innerHTML = '<span><span class="golfclub-left"></span>' + node.innerHTML + '<span class="golfclub-right"></span></span>';
    });

})();
