(function () {

    "use strict";


    //For regular mobile menu 
    document.querySelectorAll('.menu-container.mobile h2, .menu-container.phone h2').forEach(function (menu) {
        menu.addEventListener('click', function (event) {
            event.preventDefault();
            menu.closest(".menu-container").classList.toggle('active');
        })
    });


    document.querySelectorAll('.menu-container ul li a[href^="#"]').forEach(function (link) {
        link.addEventListener('click', function (event) {
            let clickTarget = event.target;
            clickTarget.closest('ul').querySelectorAll('li a').forEach(function (anchor) {
                anchor.classList.remove('menuitem-active');
            });
            clickTarget.classList.add('menuitem-active');
        })
    });

    // to select menu  from the header 
    document.querySelectorAll("li.dropdown.nav-menu span").forEach(function (span) {
        span.parentElement.querySelectorAll("a").forEach(function (link) {
            if (link.pathname == window.location.pathname && link.hash == window.location.hash) {
                span.classList.add("red");
                span.classList.add("btm-brdr-red");
            }
        });
    });

    // to select menu  from the content 
    window.addEventListener("load", function () {
        let hash = window.location.hash
        document.querySelectorAll('.menu-container ul li a[href^="#"]').forEach(function (link) {
            if (hash === link.hash) {
                link.classList.add('menuitem-active');
            }
        });
    })



})();
