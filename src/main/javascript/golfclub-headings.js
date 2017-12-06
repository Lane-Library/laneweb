(function() {

    "use strict";

    Array.prototype.forEach.call(document.querySelectorAll(".golfclub"), function(node) {
        node.innerHTML = '<span><span class="golfclub-left"></span>' + node.innerHTML + '<span class="golfclub-right"></span></span>';
    });

})();
