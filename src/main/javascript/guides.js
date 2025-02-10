(function () {

    "use strict";


    if (document.querySelector(".guide")) {

        let defaultGuide = '#all-guides',
            allGuidesClosed = '#off',
            hash,

            closeAllGuides = function () {
                document.querySelectorAll('.menu-guide ul li a, .libguides div.menuitem-active').forEach(function (div) {
                    div.classList.remove('menuitem-active');
                })
            },

            openGuide = function (hash) {
                if (hash != allGuidesClosed) {
                    document.querySelector(hash).classList.add('menuitem-active');
                }
            };

        window.addEventListener("load", function () {
            hash = window.location.hash;
            if (hash == '') {
                hash = defaultGuide;
            }
            openGuide(hash);
        }, false);

        L.on("menu-changed", function (event) {
            closeAllGuides();
            openGuide(event.hash);
        });


        document.querySelectorAll('.guide-menu-toggle.on').forEach(function (anchor) {
            anchor.addEventListener('click', function (event) {
                closeAllGuides();
                openGuide(event.currentTarget.hash);
            });
        });

        document.querySelectorAll('.guide-menu-toggle.off').forEach(function (menubutton) {
            menubutton.addEventListener('click', function () {
                closeAllGuides();
            });
        });
    }
})();
