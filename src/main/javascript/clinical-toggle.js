(function() {
    "use strict";

    var toggle = document.querySelector(".clinical-toggle");

    if (toggle) {
        toggle.addEventListener("click", L.searchIndicator.show);
    }
})();
