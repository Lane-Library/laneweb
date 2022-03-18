(function() {

    "use strict";

    if (document.querySelector(".nav-menu")) {

        var superHeader = document.querySelector("header:first-of-type"),
            blurableNodes = document.querySelectorAll(".content, footer, .mobile-screen-menu.lrg-screen-hide"),
            toggleSuperHeader = function(){
                if (superHeader) {
                    superHeader.classList.toggle('medium-screen-hide');
                }
            },
            toggleBlur = function(action){
                blurableNodes.forEach(function(node) {
                    if ("add" === action) {
                        node.classList.add('blur');
                    } else {
                        node.classList.remove('blur');
                    }
                })
            };

        document.querySelectorAll(".nav-menu").forEach(function(node) {
            node.addEventListener("click", function(event) {
                var isSmallMedia = !window.matchMedia("(min-width: 1100px)").matches,
                    nav = event.currentTarget, 
                    clickTarget = event.target,
                    navContent = nav.querySelector('.dropdown-content');
                if (isSmallMedia && navContent && !clickTarget.href) {
                    nav.classList.toggle('nav-menu-active-on-click');
                    navContent.classList.toggle('dropdown-content-on-click');
                }
            });
        });

        document.querySelectorAll(".menu-overlay, #nav-toggle-off").forEach(function(node) {
            node.addEventListener(
                "click", function() {
                    window.location.hash = "#";
                    toggleSuperHeader();
                    toggleBlur("remove");
            }, false);
        });

        document.querySelector("#nav-toggle-on").addEventListener(
            "click", function() {
                toggleSuperHeader();
                toggleBlur("add");
            }, false);

    }

})();
