(function() {

    "use strict";

    Array.prototype.forEach.call(document.querySelectorAll(".golfclub"), function(node) {
        node.innerHTML = "<span><span>" + node.innerHTML + "</span></span>";
    });

})();