(function() {

    "use strict";

    if (document.querySelectorAll(".nav-menu") !== undefined) {

        var contentNode = document.querySelector('.content'),
            superHeader = document.querySelector("header:first-of-type"),
            toggleSuperHeader = function(){
                if (superHeader) {
                    superHeader.classList.toggle('medium-screen-hide');
                }
            };

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
                contentNode.classList.remove('blur');
            }, false);

        document.querySelector("#nav-toggle-on").addEventListener(
            "click", function() {
                toggleSuperHeader();
                contentNode.classList.add('blur');
            }, false);

        document.querySelector("#nav-toggle-off").addEventListener(
            "click", function() {
                toggleSuperHeader();
                contentNode.classList.remove('blur');
            }, false);

    }

})();
