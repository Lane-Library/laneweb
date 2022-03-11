(function() {

    "use strict";

    if (document.querySelectorAll(".nav-menu") !== undefined) {

        var toggleSuperHeader = function(){
            var header = document.querySelector("header:first-of-type");
            if (header) {
                header.classList.toggle('medium-screen-hide');
            }
        }

        document.querySelectorAll(".nav-menu").forEach(function(node) {
            node.addEventListener("click", function(event) {
                var isSmallMedia = !window.matchMedia("(min-width: 1100px)").matches,
                    nav = event.currentTarget, 
                    clickTarget = event.target,
                    navContent;
                if (isSmallMedia && !clickTarget.href) {
                    navContent = nav.querySelector('.dropdown-content');
                    nav.classList.toggle('nav-menu-active-on-click');
                    navContent.classList.toggle('dropdown-content-on-click');
                }
            });
        });

        document.querySelector(".menu-overlay").addEventListener(
            "click", function() {
                window.location.hash = "#";
                toggleSuperHeader();
            }, false);

        document.querySelectorAll("#nav-toggle-on, #nav-toggle-off").forEach(function(node) {
            node.addEventListener(
                "click", function() {
                    toggleSuperHeader();
                }, false);
            });

    }

})();


