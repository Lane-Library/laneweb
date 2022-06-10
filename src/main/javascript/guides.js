(function() {

    "use strict";


    if (document.querySelector(".guide")) {


        window.addEventListener("load", function() {
            var target = window.location.hash;
            if (!target) {
                target = "#all-guides"
            }
            document.querySelector(target).classList.add('active');
            document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
                if (anchor.href.endsWith(target)) {
                    anchor.classList.add('menuitem-active');
                }
            })

        }, false);

        // Manage click the menu in the left
        document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
            anchor.addEventListener('click', function(event) {
                var menu = event.target,
                    id = menu.href.substring(menu.href.indexOf('#'));
                document.querySelectorAll('.menu-guide ul li a').forEach(function(link) {
                    link.classList.remove('menuitem-active');
                })
                anchor.classList.add('menuitem-active');
                document.querySelectorAll('.libguides div.active').forEach(function(div) {
                    div.classList.remove('active');
                });
                document.querySelector(id).classList.add('active');
            });
        });




        // Manage click the toogle  menu on eahc guides
        document.querySelectorAll('.guide-menu-toggle').forEach(function(menubutton) {
            menubutton.addEventListener('click', function(event) {
                var button = event.target,
                    target = '#' + button.dataset.target,
                    guideDiv = document.querySelector(target);
                guideDiv.classList.toggle('active');
                document.querySelectorAll('.libguides div.active').forEach(function(div) {
                    if ("#" + div.id != target) {
                        div.classList.remove('active');
                    }
                });
                document.querySelectorAll('.menu-guide ul li a').forEach(function(anchor) {
                    anchor.classList.remove('menuitem-active');
                    if (anchor.href.endsWith(target) && !button.href.endsWith('#off')) {
                        anchor.classList.add('menuitem-active');
                    }
                });
            })
        });


    }
})();
