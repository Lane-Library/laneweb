(function() {

    "use strict";


    if (document.querySelector(".guide")) {

        var defaultGuide = '#all-guides',
            allGuidesClosed = '#off',
            hash,

        closeAllGuides = function() {
            document.querySelectorAll('.menu-guide ul li a, .libguides div.menuitem-active').forEach(function(div) {
                div.classList.remove('menuitem-active');
            })
        },
            openGuide = function(hash) {
                if (hash != allGuidesClosed) {
                    document.querySelector(hash).classList.add('menuitem-active');
                    document.querySelector('.menu-guide ul li a[href="' + hash + '"]').classList.add('menuitem-active');
                }
            };

        window.addEventListener("load", function() {
            hash = window.location.hash;
            if (hash == '') {
                hash = defaultGuide;
            }
            openGuide(hash);
        }, false);

        document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
            anchor.addEventListener('click', function(event) {
                openGuide(event.target.hash);
            });
        });

        document.querySelectorAll('.guide-menu-toggle.on').forEach(function(anchor) {
            anchor.addEventListener('click', function(event) {
                closeAllGuides();
                openGuide(event.currentTarget.hash);
            });
        });

        document.querySelectorAll('.guide-menu-toggle.off').forEach(function(menubutton) {
            menubutton.addEventListener('click', function() {
                closeAllGuides();
            });
        });
    }
})();
