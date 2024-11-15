(function () {

    "use strict";

    if (document.querySelector(".nav-menu")) {

        let blurableNodes = document.querySelectorAll(".content, footer, .mobile-screen-menu.lrg-screen-hide"),
            toggleBlur = function () {
                blurableNodes.forEach(function (node) {
                    node.classList.toggle('blur');
                })
            };

        document.querySelectorAll([".menu-overlay, #nav-toggle-off", "#nav-toggle-on"]).forEach(function (node) {
            node.addEventListener(
                "click", function () {
                    window.location.hash = "#";
                    toggleBlur();
                }, false);
        });
    }

})();
